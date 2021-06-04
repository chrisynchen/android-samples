package com.chris.android_samples.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.chris.android_samples.R
import com.chris.android_samples.listener.DownloadListener
import com.chris.android_samples.singleton.SingletonManager


class MemoryLeakActivity : AppCompatActivity(), DownloadListener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MemoryLeakActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        SingletonManager.getInstance(this)
        DownloadAsyncTask(this).execute()
    }

    private inner class DownloadTask : Thread() {
        override fun run() {
            SystemClock.sleep((1000 * 5).toLong())
        }
    }

    private inner class DownloadAsyncTask(val listener: DownloadListener) :
        AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg param: Void?): Void? {
            SystemClock.sleep((2000 * 10).toLong())
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            try {
                listener.onDownloadTaskDone()
            } catch (e: Exception) {
                //doNothing
            }
        }
    }

    override fun onDownloadTaskDone() {

    }
}