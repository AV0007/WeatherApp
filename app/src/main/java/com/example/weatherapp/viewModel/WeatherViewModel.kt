package com.example.weatherapp.viewModel

import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.NetworkState
import com.example.weatherapp.util.Util
import kotlinx.coroutines.*

private const val API_KEY = "48372bde0e3644aab1a72359221512"
private const val DAYS = 1

class WeatherViewModel constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    /*
    //live data for error
    */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    /*
    //live data to get weather response data
    */
    val weatherDataList = MutableLiveData<WeatherModel>()

    private var job: Job? = null

    private val homeLocation = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    //function to get weather data
    fun getWeatherData() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = homeLocation.value?.let {
                weatherRepository.getData(
                    API_KEY, DAYS,
                    it
                )
            }
            withContext(Dispatchers.Main) {
                loading.value = true
                when (response) {
                    is NetworkState.Success -> {
                        loading.value = false
                        weatherDataList.value = response.data
                    }
                    is NetworkState.Error -> {
                        onError("Error : ${response.response.message()}")

                    }
                    else -> {

                    }
                }
            }
        }

    }

    //function returns the appended string with celcius
    fun getTemp(temperature: Double): String {
        return "${temperature.toInt()}${Util.degreeCelcius()}"
    }

    //function returns the imageurl
    fun getImageFromUrl(url: String?): String {
        return if (!url.isNullOrEmpty()) "http:".plus(url) else ""

    }

    //function called when there will be text change
    fun onTextChange(str: Editable) {
        if (str.isNotEmpty()) {
            homeLocation.postValue(str.toString())
        }
    }

    //function called when Done ime option will be clicked
    fun onDoneClicked(view: View, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            getWeatherData()
            return true
        }
        return false
    }

    //function called at the of location fetching
    fun setLocationValue(currentLocation: String) {
        homeLocation.postValue(currentLocation)
        getWeatherData()
    }


}