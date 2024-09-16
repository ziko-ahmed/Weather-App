package com.example.weather.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.WeatherResponse
import com.example.weather.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> get() = _weather

    private val repository = WeatherRepository()

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val weatherResponse = repository.getWeather(lat, lon)
            _weather.postValue(weatherResponse)
        }
    }
}
