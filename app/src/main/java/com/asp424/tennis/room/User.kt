package com.asp424.tennis.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "patronymic") val patronymic: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "secondName") val secondName: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "city_of_training") val city_of_training: String?,
    @ColumnInfo(name = "name_child") val name_child: String?,
    @ColumnInfo(name = "secondName_child") val secondName_child: String?,
    @ColumnInfo(name = "years_old_child") val years_old_child: String?,
    @ColumnInfo(name = "token") val token: String?,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "name_group") val name_group: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "years") val years: String?,
    @ColumnInfo(name = "e_mail") val e_mail: String?,
    @ColumnInfo(name = "school") val school: String?,

    )

@Entity
data class UserBig(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "patronymic") val patronymic: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "secondName") val secondName: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "city_of_training") val city_of_training: String?,
    @ColumnInfo(name = "token") val token: String?,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "name_group") val name_group: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "e_mail") val e_mail: String?,
    @ColumnInfo(name = "school") val school: String?,
    )
