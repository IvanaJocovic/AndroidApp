package com.ivanajocovic.weather.networking.api

import com.ivanajocovic.weather.networking.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("/v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("hourly", encoded = true) hourly: String?,
        @Query("daily", encoded = true) daily: String?,
        @Query("timezone") timezone: String?
    ): WeatherResponse
}