package com.asp424.tennis.models

import androidx.compose.ui.graphics.Color
import com.asp424.tennis.utils.getEventModel
import java.time.LocalDateTime

data class EventModel(
    val name: String = "",
    val color: Color = Color.White,
    val start: LocalDateTime = LocalDateTime.parse("2000-00-00T00:00:00") ,
    val end: LocalDateTime = LocalDateTime.parse("2000-00-00T00:00:00"),
    val desc: String? = "",
    val client: String? = "",


)

data class EventBD(
    val name: String = "",
    val color: Int = 0,
    val start: String = "",
    val end: String = "",
    val desc: String? = "",
    val client: String? = "",
    val trainer: String? = "",
    )
