package com.chris.android_samples.singleton

import android.content.Context

class SingletonManager(val context: Context) {
    private val temp = IntArray(10000000)

    companion object {
        private var singleton: SingletonManager? = null

        @Synchronized
        fun getInstance(context: Context): SingletonManager? {
            if (singleton == null) {
                singleton = SingletonManager(context)
            }
            return singleton
        }
    }
}