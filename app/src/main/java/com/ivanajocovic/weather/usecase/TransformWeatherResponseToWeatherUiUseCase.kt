package com.ivanajocovic.weather.usecase

import com.ivanajocovic.weather.networking.dto.WeatherResponse
import com.ivanajocovic.weather.ui.WeatherCode
import com.ivanajocovic.weather.ui.WeatherDayUi
import com.ivanajocovic.weather.ui.WeatherHourlyUi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class TransformWeatherResponseToWeatherUiUseCase @Inject constructor() {

    operator fun invoke(response: WeatherResponse): List<WeatherDayUi> {

        val hourlyList = mutableListOf<WeatherHourlyUi>()
        val dailyList = mutableListOf<WeatherDayUi>()

        response.hourly?.time?.mapIndexed { index, time ->
            hourlyList.add(
                WeatherHourlyUi(
                    time = time,
                    weatherCode = WeatherCode(code = response.hourly.weatherCode?.get(index)),
                    temperature = response.hourly.temperature2m?.get(index),
                    isCurrent = ChronoUnit.MINUTES.between(time, LocalDateTime.now()) < 60
                )
            )
        }

        response.daily?.time?.mapIndexed { index, localDate ->
            dailyList.add(
                WeatherDayUi(
                    date = localDate,
                    sunrise = response.daily.sunrise?.get(index),
                    sunset = response.daily.sunset?.get(index),
                    temperatureMax = response.daily.temperature2mMax?.get(index),
                    temperatureMin = response.daily.temperature2mMin?.get(index),
                    weatherCode = WeatherCode(code = response.daily.weatherCode?.get(index)),
                    hourlyUi = hourlyList.filter { it.time?.toLocalDate() == localDate }
                )
            )
        }

        return dailyList
    }
}