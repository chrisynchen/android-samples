package com.chris.android_samples.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chris.android_samples.broadcast.MessageBroadcastReceiver
import java.util.concurrent.TimeUnit


// lifecycle: onCreate -> onStartCommand -> service running -> onDestroy
class StartServiceExperiment : Service() {

    private lateinit var countDownTimer: CountDownTimer

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, StartServiceExperiment::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "service onCreate", Toast.LENGTH_SHORT).show()
        countDownTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(1)) {
                override fun onTick(millisUntilFinished: Long) {
                    val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    val intent = Intent(MessageBroadcastReceiver.FILTER).apply {
                        putExtra(MessageBroadcastReceiver.SERVICE_MESSAGE, second)
                    }
                    LocalBroadcastManager.getInstance(this@StartServiceExperiment)
                        .sendBroadcast(intent)
                }

                override fun onFinish() {
                    val intent = Intent(MessageBroadcastReceiver.FILTER).apply {
                        putExtra(MessageBroadcastReceiver.SERVICE_COUNTER_FINISH, true)
                    }
                    LocalBroadcastManager.getInstance(this@StartServiceExperiment)
                        .sendBroadcast(intent)
                }
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service onStartCommand", Toast.LENGTH_SHORT).show()
        countDownTimer.start()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(this, "service onBind", Toast.LENGTH_SHORT).show()
        throw UnsupportedOperationException("we don't provide bind service for this class")
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
        Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}