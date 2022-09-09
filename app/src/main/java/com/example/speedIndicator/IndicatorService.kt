package com.example.speedIndicator

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.net.TrafficStats
import android.os.Binder
import android.os.Handler
import android.os.IBinder

class IndicatorService : Service() {
  private var speedUpdateCallback: SpeedUpdateCallback? = null
  private var mLastRxBytes: Long = 0
  private var mLastTxBytes: Long = 0
  private var mLastTime: Long = 0
  private var mSpeed: Speed? = null
  private val mBinder:IBinder = SpeedServiceBinder()
  private val mHandler = Handler()
  private val mHandlerRunnable: Runnable = object : Runnable {
    override fun run() {
      val currentRxBytes = TrafficStats.getTotalRxBytes()
      val currentTxBytes = TrafficStats.getTotalTxBytes()
      val usedRxBytes = currentRxBytes - mLastRxBytes
      val usedTxBytes = currentTxBytes - mLastTxBytes
      val currentTime = System.currentTimeMillis()
      val usedTime = currentTime - mLastTime
      mLastRxBytes = currentRxBytes
      mLastTxBytes = currentTxBytes
      mLastTime = currentTime
      mSpeed!!.calcSpeed(usedTime, usedRxBytes, usedTxBytes)
      speedUpdateCallback!!.onSpeedUpdate(mSpeed!!)
      mHandler.postDelayed(this,500)
    }
  }

  inner class SpeedServiceBinder : Binder() {
    fun getService():IndicatorService{
      return this@IndicatorService
    }
    fun restartNotifying() {
      mHandler.removeCallbacks(mHandlerRunnable)
      mHandler.post(mHandlerRunnable)
    }
  }

  override fun onBind(intent: Intent): IBinder {
    return mBinder
  }

  override fun onUnbind(intent: Intent): Boolean {
    pauseNotifying()
    return false
  }

  override fun onCreate() {
    super.onCreate()
    mSpeed= Speed(this)
  }

  override fun onDestroy() {
    pauseNotifying()
    super.onDestroy()
  }

  //    @Override
  //    public int onStartCommand(Intent intent, int flags, int startId) {
  //        handleConfigChange(intent.getExtras());
  //
  //        createNotification();
  //
  //        restartNotifying();
  //
  //        return START_REDELIVER_INTENT;
  //    }

  private fun pauseNotifying() {
    mHandler.removeCallbacks(mHandlerRunnable)
  }

//  private fun handleConfigChange(config: Bundle) {
//    // Show/Hide on lock screen
//    val screenBroadcastIntentFilter = IntentFilter()
//    screenBroadcastIntentFilter.addAction(Intent.ACTION_SCREEN_ON)
//    screenBroadcastIntentFilter.addAction(Intent.ACTION_SCREEN_OFF)
//    mNotificationOnLockScreen = config.getBoolean(SettingsActivity.KEY_NOTIFICATION_ON_LOCK_SCREEN, false)
//    if (!mNotificationOnLockScreen) {
//      screenBroadcastIntentFilter.addAction(Intent.ACTION_USER_PRESENT)
//      screenBroadcastIntentFilter.priority = 999
//    }
//    if (mNotificationCreated) {
//      unregisterReceiver(mScreenBroadcastReceiver)
//    }
//    registerReceiver(mScreenBroadcastReceiver, screenBroadcastIntentFilter)
//
//    // Speed unit, bps or Bps
//    val isSpeedUnitBits = config.getString(SettingsActivity.KEY_INDICATOR_SPEED_UNIT, "Bps") == "bps"
//    mSpeed!!.setIsSpeedUnitBits(isSpeedUnitBits)
//
//    // Pass it to notification
//    mIndicatorNotification!!.handleConfigChange(config)
//  }

  fun setActivityReference(activity: Activity) {
    this.speedUpdateCallback = activity as SpeedShowActivity
  }
}