package com.example.weatherapp.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.adapter.MainAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.factory.WeatherViewModelFactory
import com.example.weatherapp.location.PermissionUtils
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.RetrofitService
import com.example.weatherapp.util.Util
import com.example.weatherapp.util.Util.Companion.hideKeyboard
import com.example.weatherapp.viewModel.WeatherViewModel
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private val adapter = MainAdapter()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val retrofit = RetrofitService.getInstance()
        val repository = WeatherRepository(retrofit)
        viewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository)
        )[WeatherViewModel::class.java]
        binding.rvForcast.adapter = adapter
        binding.weather = viewModel
        binding.lifecycleOwner = this

        /*  //observing the data for weather api*/
        viewModel.weatherDataList.observe(this) {
            hideKeyboard()
            it.forecast?.forecastday?.first()?.let { it1 -> adapter.setWeatherData(it1.hour) }
            binding.etCityName.clearFocus()
            binding.etCityName.text?.clear()
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) {
            if (it) {
                binding.pregressBar.visibility = View.VISIBLE

            } else {
                binding.pregressBar.visibility = View.GONE
            }

        }
    }

    /*  //fetching the location of user with FusedLocation*/
    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                viewModel.setLocationValue(Util.getCityName(location, this@MainActivity))
            }
        }
    }

    /*  //checking for location permission*/
    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    //checking for permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


}