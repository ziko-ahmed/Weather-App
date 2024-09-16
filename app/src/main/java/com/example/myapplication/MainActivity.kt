package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weather.data.model.WeatherResponse
import com.example.weather.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationSpinner: Spinner
    private lateinit var currentTemp: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var hourlyForecastContainer: LinearLayout
    private lateinit var dailyForecastContainer: LinearLayout
    private lateinit var rainChanceTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var updatedTimeTextView: TextView
    private lateinit var locationTextView: TextView

    private lateinit var viewModel: WeatherViewModel

    private val cityTimeZones = mapOf(
        "New York, USA" to "America/New_York",
        "London, UK" to "Europe/London",
        "Tokyo, Japan" to "Asia/Tokyo",
        "Sydney, Australia" to "Australia/Sydney",
        "Berlin, Germany" to "Europe/Berlin",
        "Paris, France" to "Europe/Paris",
        "Moscow, Russia" to "Europe/Moscow",
        "Beijing, China" to "Asia/Shanghai",
        "New Delhi, India" to "Asia/Kolkata",
        "Rio de Janeiro, Brazil" to "America/Sao_Paulo",
        "Cape Town, South Africa" to "Africa/Johannesburg",
        "Toronto, Canada" to "America/Toronto",
        "Dubai, UAE" to "Asia/Dubai",
        "Hong Kong, China" to "Asia/Hong_Kong",
        "Istanbul, Turkey" to "Europe/Istanbul",
        "Mexico City, Mexico" to "America/Mexico_City",
        "Buenos Aires, Argentina" to "America/Argentina/Buenos_Aires",
        "Seoul, South Korea" to "Asia/Seoul",
        "Singapore" to "Asia/Singapore",
        "Bangkok, Thailand" to "Asia/Bangkok"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationSpinner = findViewById(R.id.locationSpinner)
        currentTemp = findViewById(R.id.currentTemp)
        weatherIcon = findViewById(R.id.weatherIcon)
        hourlyForecastContainer = findViewById(R.id.hourlyForecastContainer)
        dailyForecastContainer = findViewById(R.id.dailyForecastContainer)
        rainChanceTextView = findViewById(R.id.rainChanceTextView)
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView)
        updatedTimeTextView = findViewById(R.id.updatedTimeTextView)
        locationTextView = findViewById(R.id.locationTextView)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        setupLocationSpinner()

        observeViewModel()
    }

    private fun setupLocationSpinner() {
        val locations = arrayOf(
            "New York, USA", "London, UK", "Tokyo, Japan",
            "Sydney, Australia", "Berlin, Germany", "Paris, France",
            "Moscow, Russia", "Beijing, China", "New Delhi, India",
            "Rio de Janeiro, Brazil", "Cape Town, South Africa", "Toronto, Canada",
            "Dubai, UAE", "Hong Kong, China", "Istanbul, Turkey",
            "Mexico City, Mexico", "Buenos Aires, Argentina", "Seoul, South Korea",
            "Singapore", "Bangkok, Thailand"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        locationSpinner.adapter = adapter

        locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val location = locations[position]
                val (city, _) = location.split(", ", limit = 2)
                val coordinates = when (city) {
                    "New York" -> Pair(40.7128, -74.0060)
                    "London" -> Pair(51.5074, -0.1278)
                    "Tokyo" -> Pair(35.6762, 139.6503)
                    "Sydney" -> Pair(-33.8688, 151.2093)
                    "Berlin" -> Pair(52.52, 13.4050)
                    "Paris" -> Pair(48.8566, 2.3522)
                    "Moscow" -> Pair(55.7558, 37.6176)
                    "Beijing" -> Pair(39.9042, 116.4074)
                    "New Delhi" -> Pair(28.6139, 77.2090)
                    "Rio de Janeiro" -> Pair(-22.9068, -43.1729)
                    "Cape Town" -> Pair(-33.9249, 18.4241)
                    "Toronto" -> Pair(43.6510, -79.3470)
                    "Dubai" -> Pair(25.276987, 55.296249)
                    "Hong Kong" -> Pair(22.3193, 114.1694)
                    "Istanbul" -> Pair(41.0082, 28.9784)
                    "Mexico City" -> Pair(19.4326, -99.1332)
                    "Buenos Aires" -> Pair(-34.6037, -58.3816)
                    "Seoul" -> Pair(37.5665, 126.9780)
                    "Singapore" -> Pair(1.3521, 103.8198)
                    "Bangkok" -> Pair(13.7563, 100.5018)
                    else -> Pair(0.0, 0.0) // Default coordinates
                }
                viewModel.fetchWeather(coordinates.first, coordinates.second)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when no location is selected (optional)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.weather.observe(this) { weather ->
            updateUI(weather)
        }
    }

    private fun updateUI(weather: WeatherResponse) {
        currentTemp.text = "${weather.current_weather.temperature}°C"
        locationTextView.text = locationSpinner.selectedItem.toString()
        updateWeatherIcon(weather.current_weather.weathercode)
        rainChanceTextView.text = "${weather.current_weather.rainChance}%"
        feelsLikeTextView.text = "Feels like: ${weather.current_weather.feelsLike}°C"

        // Get the city's time zone
        val city = locationSpinner.selectedItem.toString()
        val timeZoneId = cityTimeZones[city] ?: "UTC" // Default to UTC if city not found
        val timeZone = TimeZone.getTimeZone(timeZoneId)

        // Get the current time in the city's time zone
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdf.timeZone = timeZone
        val currentTime = sdf.format(Date())

        updatedTimeTextView.text = "Current time: $currentTime"

        // Update forecast containers as before
        hourlyForecastContainer.removeAllViews()
        weather.hourly.temperature_2m.forEach { temp ->
            val view = layoutInflater.inflate(R.layout.forecast_item, hourlyForecastContainer, false)
            val tempTextView = view.findViewById<TextView>(R.id.forecastTemp)
            tempTextView.text = "${temp}°C"
            hourlyForecastContainer.addView(view)
        }

        dailyForecastContainer.removeAllViews()
        weather.daily.temperature_2m_max.forEachIndexed { index, maxTemp ->
            val view = layoutInflater.inflate(R.layout.forecast_item, dailyForecastContainer, false)
            val tempTextView = view.findViewById<TextView>(R.id.forecastTemp)
            tempTextView.text = "Max: ${maxTemp}°C, Min: ${weather.daily.temperature_2m_min[index]}°C"
            dailyForecastContainer.addView(view)
        }
    }

    private fun updateWeatherIcon(weatherCode: Int) {
        val iconRes = when (weatherCode) {
            0 -> R.drawable.sun
            1 -> R.drawable.partly_cloudy
            2, 3 -> R.drawable.cloudy
            45, 48 -> R.drawable.fog
            51, 53, 55, 56, 57 -> R.drawable.drizzle
            61, 63 -> R.drawable.light_rain
            65 -> R.drawable.heavy_rain
            71, 73, 75 -> R.drawable.snow
            95, 96, 99 -> R.drawable.cyclone
            else -> R.drawable.default_weather_icon
        }
        weatherIcon.setImageResource(iconRes)
    }
}
