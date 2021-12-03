package com.asp424.tennis.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TrainerMod(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "e_mail") val e_mail: String?,
    @ColumnInfo(name = "gps_time") val gps_time: String?,
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "latitude") val latitude: String?,
    @ColumnInfo(name = "longitude") val longitude: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "patronymic") val patronymic: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "secondName") val secondName: String?,
    @ColumnInfo(name = "timeRegistrationStamp") val timeRegistrationStamp: String?,
    @ColumnInfo(name = "token") val token: String?,
    @ColumnInfo(name = "whatsapp_number") val whatsapp_number: String?,
    @ColumnInfo(name = "workPlace") val workPlace: String?,

    )