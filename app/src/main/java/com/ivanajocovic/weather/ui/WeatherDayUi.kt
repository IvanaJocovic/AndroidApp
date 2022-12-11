package com.ivanajocovic.weather.ui

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime

data class WeatherDayUi(
    val date: LocalDate? = null,
    val sunrise: LocalDateTime? = null,
    val sunset: LocalDateTime? = null,
    val temperatureMax: Float? = null,
    val temperatureMin: Float? = null,
    val weatherCode: Int? = null,
    val hourlyUi: List<WeatherHourlyUi> = emptyList()
)

