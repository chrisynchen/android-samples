package com.chris.android_samples.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chris.android_samples.broadcast.MessageBroadcastReceiver
import com.chris.android_samples.R
import com.chris.android_samples.databinding.ActivityStartServiceBinding
import com.chris.android_samples.service.StartServiceExperiment

class StartServiceExperimentActivity : AppCompatActivity(),
    MessageBroadcastReceiver.Listener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, StartServiceExperimentActivity::class.java)
        }
    }

    private lateinit var binding: ActivityStartServiceBinding
    private val receiver =
        MessageBroadcastReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startButton.setOnClickListener {
            startService(StartServiceExperiment.createIntent(this))
        }

        binding.stopButton.setOnClickListener {
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
        binding.counterTextView.text = message.toString()
    }

    override fun onCounterFinish() {
        binding.counterTextView.text = getString(R.string.counter_finish)
    }
}