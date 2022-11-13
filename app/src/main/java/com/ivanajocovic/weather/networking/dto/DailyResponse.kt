package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class DailyResponse(
    val time: List<Date>? = null,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Float>? = null,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Float>? = null,
    @SerializedName("apparent_temperature_max") val apparentTemperatureMax: List<Float>? = null,
    @SerializedName("apparent_temperature_min") val apparentTemperatureMin: List<Float>? = null,
    val sunrise: Date? = null,
    val sunset: Date? = null
)
