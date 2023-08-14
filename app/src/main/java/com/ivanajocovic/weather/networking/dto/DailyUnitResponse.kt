package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName

data class DailyUnitResponse(
    val time: String? = null,
    @SerializedName("temperature_2m_max") val temperature2mMax: String? = null,
    @SerializedName("temperature_2m_min") val temperature2mMin: String? = null,
    @SerializedName("apparent_temperature_max") val apparentTemperatureMax: String? = null,
    @SerializedName("apparent_temperature_min") val apparentTemperatureMin: String? = null,
    val sunrise: String? = null,
    val sunset: String? = null

)
