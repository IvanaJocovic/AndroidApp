package com.ivanajocovic.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.ui.WeatherDayUi
import com.ivanajocovic.weather.usecase.TransformWeatherResponseToWeatherUiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataSource: WeatherDataSource,
    private val useCase: TransformWeatherResponseToWeatherUiUseCase
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
                    hourly = "temperature_2m,rain,showers,snowfall,weathercode",
                    daily = "weathercode,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset",
                    timezone = "Europe/Berlin"
                )

                val dailyUi = useCase.invoke(weatherResponse)

                _uiState.update {
                    WeatherUiState.Success(dailyUi)
                }
            } catch(e:Exception) {
                _uiState.update { WeatherUiState.Error(e) }
            }
        }
    }

    sealed class WeatherUiState {
        data class Success(val data: List<WeatherDayUi>): WeatherUiState()
        data class Error(val exception: Exception): WeatherUiState()
        object Loading: WeatherUiState()
        object NoContent: WeatherUiState()
    }
}