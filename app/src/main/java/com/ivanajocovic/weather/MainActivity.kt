package com.ivanajocovic.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivanajocovic.weather.databinding.ActivityMainBinding
import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.ui.WeatherDayUi
import com.ivanajocovic.weather.ui.WeatherListAdapter
import com.ivanajocovic.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    @Inject lateinit var dataSource: WeatherDataSource

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getWeatherInfo()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Log.i("WeatherUiStateLog", "$uiState")
                    when(uiState){
                        is WeatherViewModel.WeatherUiState.Error -> Toast.makeText(this@MainActivity, uiState.exception.message, Toast.LENGTH_LONG).show()
                        is WeatherViewModel.WeatherUiState.Loading -> {}
                        is WeatherViewModel.WeatherUiState.NoContent -> {}
                        is WeatherViewModel.WeatherUiState.Success -> populateUi(uiState.data)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateUi(data: List<WeatherDayUi>) {

        setUpHeaderUi(data[0])
        setUpSeekBar(data)
        setUpRecyclerView(data)
    }

    private fun setUpHeaderUi(data: WeatherDayUi) {

        with(binding) {

            val currentTime = java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofLocalizedTime(
                    java.time.format.FormatStyle.SHORT
                )
            )
            val currentTemp = data.hourlyUi.first { it.isCurrent }.temperature.toString()
            currentTempTxt.text = "$currentTemp °C at $currentTime"
            minTempTxt.text = "min ${data.temperatureMin.toString()} °C"
            maxTempTxt.text = "max ${data.temperatureMax.toString()} °C"
            sunriseTimeTxt.text = data.sunrise?.toLocalTime()?.format(
                java.time.format.DateTimeFormatter.ofLocalizedTime(
                    java.time.format.FormatStyle.SHORT
                )
            ).toString()
            sunsetTimeTxt.text = data.sunset?.toLocalTime()?.format(
                java.time.format.DateTimeFormatter.ofLocalizedTime(
                    java.time.format.FormatStyle.SHORT
                )
            ).toString()
        }
    }

    private fun setUpSeekBar(data: List<WeatherDayUi>) {

        with(binding) {

            seekBar.max = data[0].hourlyUi.size - 1
            seekBar.setProgress(data[0].hourlyUi.indexOfFirst { it.isCurrent }, false)
            seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {

                        val hourlyUi = data[0].hourlyUi[progress]
                        val seekbarCurrentTime = hourlyUi.time?.toLocalTime()
                            ?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                            .toString()
                        val seekBarCurrentTemp = hourlyUi.temperature.toString()
                        currentTempTxt.text = "$seekBarCurrentTemp °C at $seekbarCurrentTime"
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
        }
    }

    private fun setUpRecyclerView(data: List<WeatherDayUi>) {

        with(binding) {

            weatherDayList.adapter = WeatherListAdapter(data) { index ->

                setUpHeaderUi(data[index])
            }
            weatherDayList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}