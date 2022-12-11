package com.ivanajocovic.weather.ui

import java.time.LocalDateTime

data class WeatherHourlyUi(
    val time: LocalDateTime? = null,
    val temperature: Float? = null,
    val weatherCode: Int? = null,
    val isCurrent: Boolean = false
)
