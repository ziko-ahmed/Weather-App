package com.example.weather.data.repository

import com.example.weather.data.api.WeatherService
import com.example.weather.data.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(WeatherService::class.java)

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return service.getWeather(lat, lon)
    }
}