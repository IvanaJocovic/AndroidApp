package com.ivanajocovic.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var dataSource: WeatherDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenCreated {
            val weatherResponse = dataSource.getForecast(
                latitude = 52.52,
                longitude = 13.41,
                hourly = "temperature_2m,rain,showers,snowfall",
                daily = "temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset",
                timezone = "Europe%2FBerlin"
            )

            Log.i("WeatherResponseLog", weatherResponse.toString())
        }
    }
}