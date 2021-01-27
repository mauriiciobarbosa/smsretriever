package com.mauricio.poc.smsretriever

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MessageListener {

    companion object {
        private const val REQUEST_SMS_PERMISSION_CODE = 123
    }

    private val newSmsReceiver: BroadcastReceiver by lazy {
        SMSBroadcastReceiver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startNewWay()
    }

    private fun startNewWay() {
        val task = SmsRetriever.getClient(this).startSmsUserConsent(null)

        task.addOnSuccessListener {
            Toast.makeText(this, "Successfully started retriever, expect broadcast intent", Toast.LENGTH_SHORT).show()
            registerSMSReceiver()
        }

        task.addOnFailureListener {
            Toast.makeText(this, "Failed to start retriever, inspect Exception for more details", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSMSReceiver()
    }

    private fun registerSMSReceiver() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(newSmsReceiver, intentFilter)
    }

    private fun unregisterSMSReceiver() {
        unregisterReceiver(newSmsReceiver)
    }

    override fun onMessageReceived(consentIntent: Intent) {
        try {
            // Start activity to show consent dialog to user, activity must be started in
            // 5 minutes, otherwise you'll receive another TIMEOUT intent
            startActivityForResult(consentIntent, REQUEST_SMS_PERMISSION_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Activity não inicializada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SMS_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            textViewMessage.text = message
        } else {
            Toast.makeText(this, "não deu bom", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTimeout() {
        Toast.makeText(this, "timeout", Toast.LENGTH_SHORT).show()
    }
}