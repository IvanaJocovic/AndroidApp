package com.ivanajocovic.weather.networking.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Double? = null,
    val timezone: String? = null,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String? = null,
    val elevation: Double? = null,
    @SerializedName("hourly_units") val hourlyUnits: HourlyUnitResponse,
    val hourly: HourlyResponse? = null,
    @SerializedName("daily_units") val dailyUnits: DailyUnitResponse,
    val daily: DailyResponse? = null


)
