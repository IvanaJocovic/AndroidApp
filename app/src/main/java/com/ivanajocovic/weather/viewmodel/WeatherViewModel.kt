package com.ivanajocovic.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.networking.dto.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataSource: WeatherDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.NoContent)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun getWeatherInfo() {
        viewModelScope.launch {

            _uiState.update { WeatherUiState.Loading }
            try {
                val weatherResponse = dataSource.getForecast(
                    latitude = 52.52,
                    longitude = 13.41,
                    hourly = "temperature_2m,rain,showers,snowfall",
                    daily = "temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset",
                    timezone = "Europe/Berlin"
                )

                _uiState.update {
                    WeatherUiState.Success(weatherResponse)
                }
            } catch(e:Exception) {
                _uiState.update { WeatherUiState.Error(e) }
            }
        }
    }

    sealed class WeatherUiState {
        data class Success(val response: WeatherResponse): WeatherUiState()
        data class Error(val exception: Exception): WeatherUiState()
        object Loading: WeatherUiState()
        object NoContent: WeatherUiState()
    }
}