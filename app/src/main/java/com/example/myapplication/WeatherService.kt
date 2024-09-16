package com.example.weather.data.api

import com.example.weather.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min"
    ): WeatherResponse
}