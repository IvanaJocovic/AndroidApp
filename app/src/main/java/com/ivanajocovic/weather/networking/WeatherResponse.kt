package com.ivanajocovic.weather.networking

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerializedName("hourly_units") val hourlyUnits: HourlyUnitResponse,
    val hourly: HourlyResponse
)
