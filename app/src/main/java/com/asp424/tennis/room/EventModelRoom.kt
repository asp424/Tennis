package com.asp424.tennis.room

import androidx.annotation.NonNull
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "eventmodelroom")
data class EventModelRoom(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "start") val start: String?,
    @ColumnInfo(name = "end") val end: String?,
    @ColumnInfo(name = "desc") val desc: String?
    )