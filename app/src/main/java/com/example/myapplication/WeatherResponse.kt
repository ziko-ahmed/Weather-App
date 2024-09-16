package com.example.weather.data.model

data class WeatherResponse(
    val current_weather: CurrentWeather,
    val hourly: HourlyWeatherData,
    val daily: DailyWeatherData
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int,          // Add weathercode
    val rainChance: Double,        // Add rainChance
    val feelsLike: Double,         // Add feelsLike
    val updatedAt: String          // Add updatedAt
)

data class HourlyWeatherData(
    val temperature_2m: List<Double>
)

data class DailyWeatherData(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>
)