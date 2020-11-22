package com.chris.android_samples.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chris.android_samples.broadcast.MessageBroadcastReceiver
import com.chris.android_samples.R
import com.chris.android_samples.service.StartServiceExperiment
import kotlinx.android.synthetic.main.activity_start_service.*

class ServiceExperimentActivity : AppCompatActivity(),
    MessageBroadcastReceiver.Listener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ServiceExperimentActivity::class.java)
        }
    }

    private val receiver =
        MessageBroadcastReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_service)
        startButton.setOnClickListener {
            startService(StartServiceExperiment.createIntent(this))
        }

        stopButton.setOnClickListener {
            stopService(StartServiceExperiment.createIntent(this))
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter(MessageBroadcastReceiver.FILTER)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause()
    }

    override fun onReceiveMessage(message: Long) {
        counterTextView.text = message.toString()
    }

    override fun onCounterFinish() {
        counterTextView.text = getString(R.string.counter_finish)
    }
}