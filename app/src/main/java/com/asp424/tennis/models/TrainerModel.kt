package com.asp424.tennis.models

data class TrainerModel(
    val e_mail: String = "",
    val gps_time: Any = "",
    var id: String = "",
    var latitude: String = "данных нет",
    var longitude: String = "данных нет",
    var name: String = "",
    var patronymic: String = "",
    var phone: String = "",
    var secondName: String = "",
    var timeRegistrationStamp: Any = "",
    var token: String = "",
    var whatsapp_number: String = "",
    var workPlace: String = "",
    )
