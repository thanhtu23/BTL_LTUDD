package com.example.greenfresh.model

import java.io.Serializable

data class Product(
    var id :Int,
    var name: String,
    var thumb: String,
    var description: String,
    var price: Double,
    var calories: Int,
    var sale: Int,
    var unit: String,
    var numberInCart: Int = 0
) : Serializable