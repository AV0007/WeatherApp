package com.example.weatherapp.repository

import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.util.NetworkState
import com.example.weatherapp.util.RetrofitService

class WeatherRepository(
    private val retrofitService: RetrofitService,
) {
    /*  //here we calling the weather api */
    suspend fun getData(
        key: String,
        days: Int,
        editTextContent: String
    ): NetworkState<WeatherModel> {
        val response = retrofitService.getWeatherUpdates(
            key,
            editTextContent,
            days,
            "No",
            "No",
        )
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }

        } else {
            NetworkState.Error(response)
        }

    }
}