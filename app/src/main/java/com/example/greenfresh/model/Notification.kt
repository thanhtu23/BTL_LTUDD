package com.example.greenfresh.model

data class Notification(
    var id: Int,
    var date: String,
    var payment: String,
    var status: String,
    var address: String
) : java.io.Serializable