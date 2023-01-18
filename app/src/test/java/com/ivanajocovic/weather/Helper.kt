package com.ivanajocovic.weather

import com.ivanajocovic.weather.networking.dto.*
import com.ivanajocovic.weather.ui.WeatherCode
import com.ivanajocovic.weather.ui.WeatherDayUi
import com.ivanajocovic.weather.ui.WeatherHourlyUi
import java.time.LocalDate
import java.time.LocalDateTime

val testDate = LocalDate.now()
val testDateTime = LocalDateTime.now()

fun getWeatherUiModel() = listOf(
    WeatherDayUi(
        date = testDate,
        sunset = testDateTime,
        sunrise = testDateTime,
        temperatureMax = 20F,
        temperatureMin = 15F,
        weatherCode = WeatherCode(1),
        hourlyUi = listOf(
            WeatherHourlyUi(
                time = testDateTime,
                temperature = 20F,
                weatherCode = WeatherCode(1),
                isCurrent = true
            )
        )
    )
)

fun getWeatherResponse() = WeatherResponse(
    latitude = 0.0,
    longitude = 0.0,
    generationTimeMs = 0.0,
    utcOffsetSeconds = 0.0,
    timezone = "Berlin/Europe",
    elevation = 300.0,
    hourlyUnits = HourlyUnitResponse(),
    hourly = HourlyResponse(
        time = listOf(testDateTime),
        temperature2m = listOf(20F),
        rain = listOf(0F),
        showers = listOf(0F),
        snowfall = listOf(1F),
        weatherCode = listOf(1)
    ),
    dailyUnits = DailyUnitResponse(),
    daily = DailyResponse(
        time = listOf(testDate),
        temperature2mMax = listOf(20F),
        temperature2mMin = listOf(15F),
        apparentTemperatureMax = null,
        apparentTemperatureMin = null,
        sunrise = listOf(testDateTime),
        sunset = listOf(testDateTime),
        weatherCode = listOf(1)
    )
)