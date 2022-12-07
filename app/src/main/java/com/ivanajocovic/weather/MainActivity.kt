package com.ivanajocovic.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        setContentView(R.layout.activity_main)
        viewModel.getWeatherInfo()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Log.i("WeatherUiStateLog", "$uiState")
                    when(uiState){
                        is WeatherViewModel.WeatherUiState.Error -> Toast.makeText(this@MainActivity, uiState.exception.message, Toast.LENGTH_LONG).show()
                        is WeatherViewModel.WeatherUiState.Loading -> {}
                        is WeatherViewModel.WeatherUiState.NoContent -> {}
                        is WeatherViewModel.WeatherUiState.Success -> {}
                    }
                }
            }
        }
    }
}