package com.chris.android_samples.singleton

import android.content.Context

class SingletonManager(val context: Context) {

    companion object {
        private var singleton: SingletonManager? = null

        fun getInstance(context: Context): SingletonManager =
            singleton ?: synchronized(this) {
                // should use context.applicationContext to prevent memory leak
                singleton ?: SingletonManager(context).also {
                    singleton = it
                }
            }
    }
}