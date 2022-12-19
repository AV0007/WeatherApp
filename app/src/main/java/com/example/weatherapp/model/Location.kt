package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Location(
    @SerializedName("name") var name: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("country") var country: String? = null,
)