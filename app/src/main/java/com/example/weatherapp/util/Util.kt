package com.example.weatherapp.util

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Util {
    companion object {
        /// date and time formatter
        fun formatTime(time: String): String? {
            val input = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
            val output = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            try {
                val result = input.parse(time)
                return result?.let { output.format(it) }

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun degreeCelcius(): String {
            return "°C"

        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        //function to get city name from location
        fun getCityName(location: Location, context: Context): String {
            val cityName: String?
            val geoCoder = Geocoder(context, Locale.getDefault())
            val address = geoCoder.getFromLocation(location.latitude, location.longitude, 3)
            cityName = address?.get(0)?.locality ?: ""
            return cityName
        }


    }


}