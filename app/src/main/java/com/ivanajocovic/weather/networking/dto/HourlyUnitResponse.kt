package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName

data class HourlyUnitResponse(
    val time: String? = null,
    @SerializedName("temperature_2m") val temperature2m: String? = null,
    val rain: String? = null,
    val showers: String? = null,
    val snowfall: String? = null,
    @SerializedName("weathercode") val weatherCode: String? = null
)
