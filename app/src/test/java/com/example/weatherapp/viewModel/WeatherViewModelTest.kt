package com.example.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.Location
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.NetworkState
import com.example.weatherapp.util.RetrofitService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var weatherRepository: WeatherRepository

    @Mock
    lateinit var apiService: RetrofitService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        weatherRepository = mock(WeatherRepository::class.java)
        weatherViewModel = WeatherViewModel(weatherRepository)
    }

    @Test
    fun getAllData() {
        runBlocking {
            Mockito.`when`(weatherRepository.getData("123456", 1, "Delhi"))
                .thenReturn(
                    NetworkState.Success(
                        WeatherModel(
                            forecast = Forecast(
                            ), current = Current(
                                lastUpdated = "2022-12-18 12:00",
                                lastUpdatedEpoch = 671393600,
                                tempC = 33.0,
                                tempF = 72.0,
                                isDay = 0
                            ), location = Location(
                                name = "Mountain View",
                                region = "California",
                                country = "USA"

                            )
                        )
                    )
                )
            weatherViewModel.getWeatherData()
            val result = weatherViewModel.weatherDataList.getOrAwaitValue()
            assertEquals(WeatherModel(), result)
        }
    }

    @Test
    fun getEmptyData() {
        runBlocking {
            Mockito.`when`(weatherRepository.getData("123456", 1, "Delhi"))
                .thenReturn(NetworkState.Success(WeatherModel()))
            weatherViewModel.getWeatherData()
            val result = weatherViewModel.weatherDataList.getOrAwaitValue()
            assertEquals(WeatherModel(), result)
        }
    }


}