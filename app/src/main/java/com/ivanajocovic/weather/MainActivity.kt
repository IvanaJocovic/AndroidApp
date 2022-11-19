package com.ivanajocovic.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    @Inject lateinit var dataSource: WeatherDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getWeatherInfo()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Log.i("WeatherUiStateLog", "$uiState")
                    when(uiState){
                        is WeatherViewModel.WeatherUiState.Error -> {}
                        WeatherViewModel.WeatherUiState.Loading -> {}
                        WeatherViewModel.WeatherUiState.NoContent -> {}
                        is WeatherViewModel.WeatherUiState.Success -> {}
                    }
                }
            }
        }
    }
}