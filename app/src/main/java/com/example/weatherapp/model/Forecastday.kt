package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Forecastday(
    @SerializedName("hour") var hour: ArrayList<Hour> = arrayListOf()
)