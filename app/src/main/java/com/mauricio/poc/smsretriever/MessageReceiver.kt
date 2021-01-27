package com.mauricio.poc.smsretriever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class MessageReceiver(private val listener: MessageListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.extras
        val pdus = data?.get("pdus") as? Array<Any>

        pdus?.forEach {
            val smsMessage = SmsMessage.createFromPdu(it as ByteArray)
            val message = ("Sender : " + smsMessage.displayOriginatingAddress
                + "Email From: " + smsMessage.emailFrom
                + "Emal Body: " + smsMessage.emailBody
                + "Display message body: " + smsMessage.displayMessageBody
                + "Time in millisecond: " + smsMessage.timestampMillis
                + "Message: " + smsMessage.messageBody)

            listener.messageReceived(message)
        }
    }
}