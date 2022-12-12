package com.ivanajocovic.weather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivanajocovic.weather.databinding.ItemWeatherBinding

class WeatherListAdapter(
    private val weatherDayUi: List<WeatherDayUi>,
    private val onSelect: (Int) -> Unit
): RecyclerView.Adapter<WeatherListAdapter.WeatherDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder {

        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        val weatherDay: WeatherDayUi = weatherDayUi[position]
        holder.populateUi(weatherDay, onSelect)

    }

    override fun getItemCount(): Int {
        return weatherDayUi.size
    }

    inner class WeatherDayViewHolder(
        private val binding: ItemWeatherBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun populateUi(
            data: WeatherDayUi,
            onSelect: (Int) -> Unit
        ) {

            with(binding) {

                root.setOnClickListener { onSelect(adapterPosition) }
                itemWeatherDateTxt.text = data.date.toString()
                itemWeatherTempTxt.text = data.temperatureMax.toString() + " °C"
                itemWeatherCodeTxt.text = data.weatherCode?.toText()
            }
        }
    }
}