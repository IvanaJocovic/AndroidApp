package com.ivanajocovic.weather.ui

import com.ivanajocovic.weather.R
import java.time.LocalTime

data class WeatherCode(
    val code: Int? = null
) {

    fun toText(): String {

        return when(code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            56, 57 -> "Freezing Drizzle"
            61, 63, 65 -> "Rain"
            66, 67 -> "Freezing Rain"
            71, 73, 75 -> "Snow fall"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95, 96, 99 -> "Thunderstorm"
            else -> ""
        }
    }

    fun toImg(
        sunrise: LocalTime?,
        sunset: LocalTime?,
        currentTime: LocalTime? = LocalTime.now()
    ): Int {

        val time = currentTime ?: LocalTime.now()
        val isDay = time.isAfter(sunrise) and time.isBefore(sunset)

        return when(code) {
            0 -> if (isDay) R.raw.weather_day_clear_sky else R.raw.weather_night_clear_sky
            1, 2, 3 -> if (isDay) R.raw.weather_day_scattered_clouds else R.raw.weather_night_scattered_clouds
            45, 48 -> if (isDay) R.raw.weather_day_mist else R.raw.weather_night_mist
            51, 53, 55 -> if (isDay) R.raw.weather_day_rain else R.raw.weather_night_rain
            56, 57 -> if (isDay) R.raw.weather_day_rain else R.raw.weather_night_rain
            61, 63, 65 -> if (isDay) R.raw.weather_day_rain else R.raw.weather_night_rain
            66, 67 -> if (isDay) R.raw.weather_day_rain else R.raw.weather_night_rain
            71, 73, 75 -> if (isDay) R.raw.weather_day_snow else R.raw.weather_night_snow
            77 -> if (isDay) R.raw.weather_day_snow else R.raw.weather_night_snow
            80, 81, 82 -> if (isDay) R.raw.weather_day_shower_rains else R.raw.weather_night_shower_rains
            85, 86 -> if (isDay) R.raw.weather_day_snow else R.raw.weather_night_snow
            95, 96, 99 -> if (isDay) R.raw.weather_day_thunderstorm else R.raw.weather_night_thunderstorm
            else -> if (isDay) R.raw.weather_day_clear_sky else R.raw.weather_night_clear_sky
        }
    }

    fun toImg(): Int {

        return when(code) {
            0 -> R.raw.weather_day_clear_sky
            1, 2, 3 -> R.raw.weather_day_scattered_clouds
            45, 48 -> R.raw.weather_day_mist
            51, 53, 55 -> R.raw.weather_day_rain
            56, 57 -> R.raw.weather_day_rain
            61, 63, 65 -> R.raw.weather_day_rain
            66, 67 -> R.raw.weather_day_rain
            71, 73, 75 -> R.raw.weather_day_snow
            77 -> R.raw.weather_day_snow
            80, 81, 82 -> R.raw.weather_day_shower_rains
            85, 86 -> R.raw.weather_day_snow
            95, 96, 99 -> R.raw.weather_day_thunderstorm
            else -> R.raw.weather_day_clear_sky
        }
    }
}
