package com.mauricio.poc.smsretriever

import android.Manifest
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MessageListener {

    companion object {
        private const val REQUEST_SMS_PERMISSION_CODE = 123
    }

    private val permissionManager = PermissionManager()
    private val smsReceiver: BroadcastReceiver by lazy {
        MessageReceiver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestSmsPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSMSReceiver()
    }

    private fun requestSmsPermission() {
        permissionManager.checkPermission(this, Manifest.permission.READ_SMS,
            object : PermissionManager.PermissionAskListener {
                override fun onNeedPermission() {
                    ActivityCompat.requestPermissions(
                        this@MainActivity, arrayOf(Manifest.permission.READ_SMS),
                        REQUEST_SMS_PERMISSION_CODE
                    )
                }

                override fun onPermissionPreviouslyDenied() {
                    Toast.makeText(this@MainActivity, "Não deu permissão", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionGranted() {
                    Toast.makeText(this@MainActivity, "foi rápido", Toast.LENGTH_SHORT).show()
                    registerSMSReceiver()
                }
            })
    }

    private fun registerSMSReceiver() {
        registerReceiver(smsReceiver, IntentFilter().apply {
            addAction("android.provider.Telephony.SMS_RECEIVED")
        })
    }

    private fun unregisterSMSReceiver() {
        unregisterReceiver(smsReceiver)
    }

    override fun messageReceived(message: String) {
        textViewMessage.text = message
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_SMS_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults.first() == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "foi", Toast.LENGTH_SHORT).show()
            registerSMSReceiver()
        } else {
            Toast.makeText(this, "não deu bom", Toast.LENGTH_SHORT).show()
        }
    }
}