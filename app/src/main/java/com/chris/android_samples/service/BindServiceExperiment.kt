package com.chris.android_samples.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Process
import android.widget.Toast
import java.util.concurrent.TimeUnit


// lifecycle: onCreate -> onBind -> clients bound to service running -> onUnbind -> onDestroy
class BindServiceExperiment : Service() {

    private lateinit var countDownTimer: CountDownTimer

    private var remainTime: Long? = null

    private var listener: IRemoteServiceListener? = null

    private val binder = object : IRemoteService.Stub() {
        override fun getPid(): Int {
            return Process.myPid()
        }

        override fun getRemainingTime(): Long {
            return remainTime ?: 0
        }

        override fun setRemainingTimeListener(listener: IRemoteServiceListener?) {
            this@BindServiceExperiment.listener = listener
        }
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(applicationContext, "service onCreate", Toast.LENGTH_SHORT).show()
        countDownTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(100), TimeUnit.SECONDS.toMillis(1)) {
                override fun onTick(millisUntilFinished: Long) {
                    remainTime = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    remainTime?.let {
                        listener?.updateRemainTime(it)
                    }
                }

                override fun onFinish() {

                }
            }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        throw UnsupportedOperationException("we don't provide start service for this class")
    }

    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(applicationContext, "onBind", Toast.LENGTH_SHORT).show()
        return binder
    }

    /**
     * To address this, in Android 2.0 Service.onStart() as been deprecated
     * (though still exists and operates as it used to in previous versions of the platform).
     * It is replaced with a new Service.onStartCommand() callback that allows the service to better
     * control how the system should manage it.
     */
    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        Toast.makeText(applicationContext, "service onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}