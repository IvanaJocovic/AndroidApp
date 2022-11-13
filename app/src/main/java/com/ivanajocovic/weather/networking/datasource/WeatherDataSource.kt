package com.ivanajocovic.weather.networking.datasource

import com.ivanajocovic.weather.networking.api.WeatherApiService
import com.ivanajocovic.weather.networking.dto.WeatherResponse
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val apiService: WeatherApiService
) {

    suspend fun getForecast(
        latitude: Double?,
        longitude: Double?,
        hourly: String?,
        daily: String?,
        timezone: String?
    ): WeatherResponse {

        return apiService.getForecast(
            latitude = latitude,
            longitude = longitude,
            hourly = hourly,
            daily = daily,
            timezone = timezone
        )
    }
}
