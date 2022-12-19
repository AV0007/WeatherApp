package com.example.weatherapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewModel.WeatherViewModel

class WeatherViewModelFactory constructor(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            WeatherViewModel(this.repository) as T
        } else {
            throw  java.lang.IllegalArgumentException("ViewModel not found")
        }
    }
}