package com.chris.android_samples.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.os.RemoteException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chris.android_samples.R
import com.chris.android_samples.service.BindServiceExperiment
import com.chris.android_samples.service.IRemoteService
import com.chris.android_samples.service.IRemoteServiceListener
import kotlinx.android.synthetic.main.activity_bind_service.*

class BindServiceExperimentActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BindServiceExperimentActivity::class.java)
        }
    }

    private var remoteService: IRemoteService? = null

    private val listener: IRemoteServiceListener = object : IRemoteServiceListener.Stub() {
        override fun updateRemainTime(remainTime: Long) {
            counterTextView.text = remainTime.toString()
        }
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            remoteService = IRemoteService.Stub.asInterface(service)
            remoteService?.setRemainingTimeListener(listener)
            killProcessButton.isEnabled = true
            statusTextView.text = "onServiceConnected"
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // unbindService will not trigger this
            // This is called when the connection with the service has been
            // unexpectedly disconnected (like crash) -- that is, its process crashed.
            remoteService = null
            killProcessButton.isEnabled = false
            statusTextView.text = "onServiceDisconnected"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_service)
        startButton.setOnClickListener {
            bindService()
        }

        stopButton.setOnClickListener {
            unbindService()
        }

        killProcessButton.setOnClickListener {
            killProcess()
        }

        fetchRemainTimeButton.setOnClickListener {
            remainTimeTextView.text = remoteService?.remainingTime.toString()
        }
    }

    override fun onStop() {
        unbindService()
        super.onStop()
    }

    private fun killProcess() {
        try {
            remoteService?.pid?.also { pid ->
                // Note that, though this API allows us to request to
                // kill any process based on its PID, the kernel will
                // still impose standard restrictions on which PIDs you
                // are actually able to kill.  Typically this means only
                // the process running your application and any additional
                // processes created by that app as shown here; packages
                // sharing a common UID will also be able to kill each
                // other's processes.
                Process.killProcess(pid)
                statusTextView.text = "Killed service process."
            }
        } catch (ex: RemoteException) {
            // Recover gracefully from the process hosting the
            // server dying.
            // Just for purposes of the sample, put up a notification.
            Toast.makeText(applicationContext, "killProcess fail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindService() {
        //already bound service
        if (remoteService != null) return

        bindService(
            Intent(this, BindServiceExperiment::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindService() {
        // already unbound service
        if (remoteService == null) return

        remoteService = null
        unbindService(serviceConnection)
    }
}