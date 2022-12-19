package com.example.weatherapp.repository

import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.util.NetworkState
import com.example.weatherapp.util.RetrofitService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class WeatherRepositoryTest {

    lateinit var weatherRepository: WeatherRepository

    @Mock
    lateinit var apiService: RetrofitService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        weatherRepository = WeatherRepository(apiService)
    }

    @Test
    fun `get all data test`() {
        runBlocking {
            Mockito.`when`(
                apiService.getWeatherUpdates(
                    "123456",
                    "Delhi",
                    1,
                    alerts = "No",
                    aqi = "no"
                )
            ).thenReturn(
                Response.success(
                    WeatherModel()
                )
            )
            val response =
                weatherRepository.getData(key = "123456", editTextContent = "Delhi", days = 1)
            assertEquals(listOf<WeatherModel>(), NetworkState.Success(response).data)
        }

    }

}
