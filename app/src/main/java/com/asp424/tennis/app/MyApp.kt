package com.asp424.tennis.app

import android.app.Application
import androidx.room.Room
import com.asp424.tennis.MainActivity
import com.asp424.tennis.repository.AppFireBaseRepo
import com.asp424.tennis.room.AppDatabase

class MyApp : Application() {
    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

    }
}