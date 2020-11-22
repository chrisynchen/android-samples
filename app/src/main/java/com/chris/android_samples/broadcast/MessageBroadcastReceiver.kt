package com.chris.android_samples.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MessageBroadcastReceiver(private val listener: Listener) : BroadcastReceiver() {

    companion object {
        const val FILTER = "FILTER"
        const val SERVICE_MESSAGE = "SERVICE_MESSAGE"
        const val SERVICE_COUNTER_FINISH = "SERVICE_COUNTER_FINISH"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getLongExtra(SERVICE_MESSAGE, -1)
        val counterFinish = intent.getBooleanExtra(SERVICE_COUNTER_FINISH, false)
        if (message >= 0) {
            listener.onReceiveMessage(message)
        }

        if (counterFinish) {
            listener.onCounterFinish()
        }
    }

    interface Listener {
        fun onReceiveMessage(message: Long)
        fun onCounterFinish()
    }
}