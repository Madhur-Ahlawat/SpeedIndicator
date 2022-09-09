package com.example.speedIndicator

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView

class SpeedShowActivity : AppCompatActivity(), SpeedUpdateCallback {
  var mServiceConnection: SpeedServiceConnection? = null
  var indicatorService: IndicatorService? = null
  var tvDownLink: AppCompatTextView? = null
  var tvUpLink: AppCompatTextView? = null
  var tvSpeedUnitUplink: AppCompatTextView? = null
  var tvSpeedUnitDownlonk: AppCompatTextView? = null
  var mIntent: Intent? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_speed_show)
    initUI()
    mServiceConnection = SpeedServiceConnection(this)
    mIntent = Intent(this,IndicatorService::class.java)
    bindService(mIntent, mServiceConnection!!, Context.BIND_AUTO_CREATE)
  }

  private fun initUI() {
    tvDownLink = findViewById(R.id.tvDownLink)
    tvUpLink = findViewById(R.id.tvUpLink)
    tvSpeedUnitUplink = findViewById(R.id.tvSpeedUnitUplink)
    tvSpeedUnitDownlonk = findViewById(R.id.tvSpeedUnitDownlonk)
  }

  class SpeedServiceConnection(speedShowActivity: SpeedShowActivity) : ServiceConnection {
    var speedShowActivity = speedShowActivity
    var indicatorService: IndicatorService? = null
    var speedServiceBinder: IndicatorService.SpeedServiceBinder? = null
    override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder) {
      this.speedServiceBinder = iBinder as IndicatorService.SpeedServiceBinder
      indicatorService = this.speedServiceBinder!!.getService()
      indicatorService!!.setActivityReference(this.speedShowActivity)
      this.speedServiceBinder!!.restartNotifying()
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
      Toast.makeText(speedShowActivity,"Service stopped!",Toast.LENGTH_SHORT).show()
    }
  }

  override fun onSpeedUpdate(mSpeed: Speed) {
    updateNewSpeed(mSpeed)
  }

  private fun updateNewSpeed(mSpeed: Speed) {
    tvDownLink!!.text = mSpeed.down.speedValue
    tvSpeedUnitDownlonk!!.text = mSpeed.down.speedUnit

    tvUpLink!!.text = mSpeed.up.speedValue
    tvSpeedUnitUplink!!.text = mSpeed.up.speedUnit
  }

  override fun onDestroy() {
    super.onDestroy()
    mServiceConnection?.let { unbindService(it) }
  }
}

interface SpeedUpdateCallback {
  fun onSpeedUpdate(mSpeed: Speed)
}
