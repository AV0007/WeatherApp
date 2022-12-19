package com.example.weatherapp.util

import com.example.weatherapp.model.WeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/*  //http client Retrofit to make api calls*/

interface RetrofitService {
    @GET("forecast.json")
    suspend fun getWeatherUpdates(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String

    ): Response<WeatherModel>

    companion object {
        var retrofitService: RetrofitService? = null
        private const val basUrl = "http://api.weatherapi.com/v1/"

        fun getInstance(): RetrofitService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(basUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}