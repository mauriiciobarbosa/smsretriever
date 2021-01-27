package com.mauricio.poc.smsretriever

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionManager {

    var isFirstAskPermission = true

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldAskPermission(context: Context, permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun checkPermission(activity: Activity, permission: String, listener: PermissionAskListener) {
        if (shouldAskPermission(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
            ) {
                listener.onNeedPermission()
            } else {
                listener.onPermissionPreviouslyDenied()
            }
        } else {
            listener.onPermissionGranted()
        }
    }

    interface PermissionAskListener {
        fun onNeedPermission()
        fun onPermissionPreviouslyDenied()
        fun onPermissionGranted()
    }
}