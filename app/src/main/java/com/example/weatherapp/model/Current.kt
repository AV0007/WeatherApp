package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Current(
    @SerializedName("last_updated_epoch") var lastUpdatedEpoch: Int? = null,
    @SerializedName("last_updated") var lastUpdated: String? = null,
    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("temp_f") var tempF: Double? = null,
    @SerializedName("is_day") var isDay: Int? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),
)