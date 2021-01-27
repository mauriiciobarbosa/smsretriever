package com.mauricio.poc.smsretriever

import android.content.Intent

interface MessageListener {
    /**
     * To call this method when new message received and send back
     * @param message Message
     */
    fun onMessageReceived(consentIntent: Intent)

    fun onTimeout()
}