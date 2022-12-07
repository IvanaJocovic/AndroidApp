package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Date

data class HourlyResponse(
    val time: List<LocalDateTime>? = null,
    @SerializedName("temperature_2m") val temperature2m: List<Float>? = null,
    val rain: List<Float>? = null,
    val showers: List<Float>? = null,
    val snowfall: List<Float>? = null
)
