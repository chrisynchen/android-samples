package com.chris.android_samples

import android.os.Handler
import android.os.Message

class ResponseHandler(private val listener: Listener) : Handler() {

    companion object {
        const val SERVICE_MESSAGE = 0;
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        when (msg.what) {
            SERVICE_MESSAGE -> {
                var message = ""
                msg.obj?.let {
                    if (it is String) {
                        message = it
                    }
                }
                listener.onReceiveMessage(message)
            }
        }
    }

    interface Listener {
        fun onReceiveMessage(message: String)
    }
}