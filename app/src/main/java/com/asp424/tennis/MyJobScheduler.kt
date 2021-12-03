package com.asp424.tennis

import android.Manifest
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.asp424.tennis.permissions.*
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MyJobScheduler : JobService(), LocationListener {
    private var locationManager: LocationManager? = null
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("My", "Job started")
        startGps(params)
        return true
    }

    private fun startGps(params: JobParameters?) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            init()
            delay(40000L)
            onStopJob(params)
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        locationManager?.removeUpdates(this@MyJobScheduler)
        return true
    }

    private fun init() {
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (locationManager?.isProviderEnabled("gps") == true && AUTH.currentUser?.uid != null)
                locationManager?.requestLocationUpdates("gps", 1000L, 0.5F, this)
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addNotification("Старт сервиса")
            }
            else addNotificationOld("Cnfhn")*/
        }
    }
    override fun onLocationChanged(location: Location) {
        try {
            val dateMap = mutableMapOf<String, Any>()
            dateMap["longitude"] = location.longitude.toString()
            dateMap["latitude"] = location.latitude.toString()
            dateMap["gps_time"] = ServerValue.TIMESTAMP
            REF_DATABASE_ROOT.child(NODE_TRAINERS).child(CURRENT_UID).updateChildren(
                dateMap
            )
        } catch (e: IOException) {
        }
    }
    /*
    private fun addNotification(text: String) = CoroutineScope(Dispatchers.IO).launch {
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val builder = NotificationCompat.Builder(this@MyJobScheduler, "123")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentText(text)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.not)
            val descriptionText = getString(R.string.content_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("123", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            with(NotificationManagerCompat.from(this@MyJobScheduler)) {
                notify(123, builder.build())
            }
        }
    }
    private fun addNotificationOld(text:String) {
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background)

            .setContentText(text)
            .setSound(defaultSoundUri)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }*/

}
