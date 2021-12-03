package com.asp424.tennis.utils

import android.content.Context
import android.widget.Toast
import com.asp424.tennis.MainActivity
import com.asp424.tennis.app.MyApp
import com.asp424.tennis.models.*
import com.asp424.tennis.room.UserDao
import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String, context: MainActivity) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun deleteSymbols(string: String?): String? {
    val first = string?.replace(Regex(" "), "")
    return first?.replace(Regex(",_-."), "")
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun String.asDate(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("dd:MM:yy", Locale.getDefault())
    return timeFormat.format(time)
}

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun DataSnapshot.getMessageModel(): ModelMessage =
    this.getValue(ModelMessage::class.java) ?: ModelMessage()

fun DataSnapshot.getEventModel(): EventBD =
    this.getValue(EventBD::class.java) ?: EventBD()





fun DataSnapshot.getTrainerModel(): TrainerModel =
    this.getValue(TrainerModel::class.java) ?: TrainerModel()

fun setStateScreen(mainActivity: MainActivity, screen: Int) {
    val sharedPref = mainActivity.getSharedPreferences("state", 0x0000) ?: return
    with(sharedPref.edit()) {
        putInt("state", screen)
        apply()
    }
}

fun getStateScreen(mainActivity: MainActivity): Int {
    val sharedPref = mainActivity.getSharedPreferences("state", 0x0000)
    return sharedPref.getInt("state", 0)
}

val Context.appData: UserDao
    get() = when (this) {
        is MyApp -> db.userDao()
        else -> this.applicationContext.appData
    }

fun getYearString(years: Int): String{
    return when(years){
        1 -> " год"
        2 -> " года"
        3 -> " года"
        4 -> " года"
        5 -> " лет"
        6 -> " лет"
        7 -> " лет"
        8 -> " лет"
        9 -> " лет"
        10 -> " лет"
        11-> " лет"
        12 -> " лет"
        13-> " лет"
        14 -> " лет"
        15 -> " лет"
        16 -> " лет"
        17 -> " лет"
        18 -> " лет"
        19 -> " лет"
        20-> " лет"
        21 -> " год"
        22 -> " года"
        23 -> " года"
        24-> " года"
        25 -> " лет"
        26 -> " лет"
        27-> " лет"
        28 -> " лет"
        29 -> " лет"
        30 -> " лет"
        31 -> " год"
        32 -> " года"
        33 -> " года"
        34 -> " года"
        35 -> " лет"
        36 -> " лет"
        37 -> " лет"
        38 -> " лет"
        39 -> " лет"
        40 -> " лет"
        41 -> " год"
        42 -> " года"
        43 -> " года"
        44 -> " года"
        45 -> " лет"
        46 -> " лет"
        47 -> " лет"
        48 -> " лет"
        49 -> " лет"
        50 -> " лет"
        51 -> " год"
        52 -> " года"
        53 -> " года"
        54 -> " года"
        55 -> " лет"
        56 -> " лет"
        57 -> " лет"
        58 -> " лет"
        59 -> " лет"
        60 -> " лет"
        61 -> " год"
        62 -> " года"
        63 -> " года"
        64 -> " года"
        65 -> " лет"
        66 -> " лет"
        67 -> " лет"
        68 -> " лет"
        69 -> " лет"
        70 -> " лет"
        71 -> " год"
        72 -> " года"
        73 -> " года"
        74 -> " года"
        75 -> " лет"
        76 -> " лет"
        77 -> " лет"
        78 -> " лет"
        79 -> " лет"
        80 -> " лет"
        81 -> " год"
        82 -> " года"
        83 -> " года"
        84 -> " года"
        85 -> " лет"
        86 -> " лет"
        87 -> " лет"
        88 -> " лет"
        89 -> " лет"
        90 -> " лет"
        else -> " лет"
    }
}
