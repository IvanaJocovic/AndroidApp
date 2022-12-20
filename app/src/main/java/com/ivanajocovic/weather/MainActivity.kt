package com.ivanajocovic.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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

    private var currentIndex = 0
    private val viewModel: WeatherViewModel by viewModels()
    @Inject lateinit var dataSource: WeatherDataSource
    private lateinit var binding: ActivityMainBinding
    private lateinit var seekBarChangeListener: OnSeekBarChangeListener

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

        setUpHeaderUi(data[0], true)
        setUpSeekBar(data[0], 0)
        seekBarChangeListener = setUpSeekBarListener(data)
        binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        setUpRecyclerView(data)
    }

    private fun setUpSeekBarListener(data: List<WeatherDayUi>): SeekBar.OnSeekBarChangeListener {

        binding.seekBar.max = data[0].hourlyUi.size - 1

        return object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {

                val hourlyUi = data[currentIndex].hourlyUi[progress]

                binding.mainScreen.background = getDrawable(
                    if (hourlyUi.time?.isAfter(data[currentIndex].sunrise) == true &&
                        hourlyUi.time.isBefore(data[currentIndex].sunset)
                    ) {
                        R.drawable.after_noon
                    } else {
                        R.drawable.night
                    }
                )


                val seekbarCurrentTime = hourlyUi.time?.toLocalTime()
                    ?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    .toString()
                val seekBarCurrentTemp = hourlyUi.temperature.toString()
                binding.currentTempTxt.text = "$seekBarCurrentTemp °C at $seekbarCurrentTime"

                if (fromUser.not()) {

                    binding.weatherCodeImg.setAnimation(
                        data[currentIndex].weatherCode?.toImg(
                            sunrise = data[currentIndex].sunrise?.toLocalTime(),
                            sunset = data[currentIndex].sunset?.toLocalTime(),
                            currentTime = hourlyUi.time?.toLocalTime()
                        ) ?: R.raw.weather_day_clear_sky
                    )
                    binding.weatherCodeImg.playAnimation()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                val hourlyUi = data[currentIndex].hourlyUi[seekBar.progress]

                binding.weatherCodeImg.setAnimation(
                    data[currentIndex].weatherCode?.toImg(
                        sunrise = data[currentIndex].sunrise?.toLocalTime(),
                        sunset = data[currentIndex].sunset?.toLocalTime(),
                        currentTime = hourlyUi.time?.toLocalTime()
                    ) ?: R.raw.weather_day_clear_sky
                )
                binding.weatherCodeImg.playAnimation()
            }
        }
    }

    private fun setUpHeaderUi(data: WeatherDayUi, isFirst: Boolean) {

        with(binding) {

            val currentTime = if (isFirst) {
                LocalTime.now()
            } else {
                data.hourlyUi[binding.seekBar.progress].time?.toLocalTime()
            } ?: LocalTime.now()

            mainScreen.background = getDrawable(
                    if (currentTime?.isAfter(data.sunrise?.toLocalTime()) == true &&
                        currentTime?.isBefore(data.sunset?.toLocalTime()) == true) {
                        R.drawable.after_noon
                    } else {
                        R.drawable.night
                    }
            )

            weatherCodeImg.setAnimation(
                data.weatherCode?.toImg(
                    sunrise = data.sunrise?.toLocalTime(),
                    sunset = data.sunset?.toLocalTime(),
                    currentTime = if (isFirst) LocalTime.now() else data.hourlyUi[binding.seekBar.progress].time?.toLocalTime()
                ) ?: R.raw.weather_day_clear_sky
            )
            weatherCodeImg.playAnimation()
            


            val currentTemp = if (isFirst) {
                data.hourlyUi.first { it.isCurrent }.temperature.toString()
            } else {
                data.hourlyUi[binding.seekBar.progress].temperature.toString()
            }
            val currentTimeTxt = currentTime?.format(
                DateTimeFormatter.ofLocalizedTime(
                    FormatStyle.SHORT
                )
            )
            currentTempTxt.text = "$currentTemp °C at $currentTimeTxt"
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

    private fun setUpSeekBar(data: WeatherDayUi, index: Int) {

        with(binding) {
            if (index != 0) {
                seekBar.setProgress((seekBar.max / 2) + 1, true)
            } else {
                seekBar.setProgress(data.hourlyUi.indexOfFirst { it.isCurrent }, true)
            }
        }
    }

    private fun setUpRecyclerView(data: List<WeatherDayUi>) {

        with(binding) {

            weatherDayList.adapter = WeatherListAdapter(data) { index ->

                currentIndex = index
                setUpHeaderUi(data[index], index == 0)
                setUpSeekBar(data[index], index)
            }
            weatherDayList.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}