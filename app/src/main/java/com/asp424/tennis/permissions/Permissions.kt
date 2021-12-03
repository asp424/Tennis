package com.asp424.tennis.permissions

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asp424.tennis.MainActivity


const val GPS = android.Manifest.permission.ACCESS_FINE_LOCATION
const val GPS_1 = android.Manifest.permission.ACCESS_COARSE_LOCATION
const val PERMISSION_REQUEST = 200
const val PERMISSION_REQUEST1 = 300

fun checkPermissions(
    permission: String,
    mainActivity: MainActivity,
    reqCode: Int
): Boolean {
    return if (ContextCompat.checkSelfPermission(
            mainActivity,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(permission),
            reqCode
        )
        false
    } else true
}
