package com.asp424.tennis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asp424.tennis.room.AppDatabase
import com.asp424.tennis.room.UserDao

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val db: UserDao) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}