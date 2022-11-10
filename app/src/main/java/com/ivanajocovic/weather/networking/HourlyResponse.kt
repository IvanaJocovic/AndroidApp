package com.ivanajocovic.weather.networking

import java.util.Date

data class HourlyResponse(
    val time: List<Date>? = null,
)
