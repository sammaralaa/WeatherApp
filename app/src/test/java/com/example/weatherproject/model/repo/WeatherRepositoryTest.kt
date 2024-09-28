package com.example.weatherproject.model.repo

import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Coord
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.Sys
import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.model.local.FakeLocalDataSource
import com.example.weatherproject.model.shared_preferences.FakeSharedDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.remote.FakeRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {
    val weather1 = WeatherModel("Cairo", 30.0, 31.0, "en", "metric")
    val weather2 = WeatherModel("London", 51.5, -0.1, "en", "metric")
    val localWeatherList = listOf(weather1, weather2)

    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var weatherRepository: WeatherRepository
    private lateinit var fakeWeatherResponse: WeatherResponse

    val coord = Coord(lon = 56.78, lat = 12.34)
    val weatherList = listOf(
        Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
    )
    val main = Main(temp = 20.0, feels_like = 19.0, temp_min = 18.0, temp_max = 22.0, pressure = 1012, humidity = 60,0,0)
    val wind = Wind(speed = 3.1, deg = 200,0.0)
    val clouds = Clouds(all = 0)
    val sys = Sys(type = 1, id = 1234, country = "US", sunrise = 1618317040, sunset = 1618361500)


    @Before
    fun setup() {
        // Initialize fake data sources with test data
        fakeLocalDataSource = FakeLocalDataSource(localWeatherList.toMutableList())

        fakeWeatherResponse = WeatherResponse(
            coord = coord,
            weather = weatherList,
            base = "stations",
            main = main,
            visibility = 10000,
            wind = wind,
            clouds = clouds,
            dt = 1618317040,
            sys = sys,
            timezone = -14400,
            id = 5128581,
            name = "New York",
            cod = 200
        )
        fakeRemoteDataSource = FakeRemoteDataSource(fakeWeatherResponse)
        // Initialize the repository with fake data sources
        weatherRepository = WeatherRepository(fakeRemoteDataSource, fakeLocalDataSource, FakeSharedDataSource())
    }
    @Test
     fun getLocalWeathers_returnsLocalWeatherData() = runTest {
        // When: fetching local weathers
        val result = weatherRepository.getLocalWeathers().toList()
        assertThat(result, `is`(listOf(localWeatherList)))

    }

    @Test
    fun insertWeather_addsWeatherToLocalDataSource() = runTest {
        // Given: a new weather model to insert
        val newWeather = WeatherModel("New York", 40.7, -74.0, "en", "metric")

        // When: inserting the new weather
        weatherRepository.insertWeather(newWeather)

        // Then: the local data source should contain the new weather
        val result = weatherRepository.getLocalWeathers().first()
        assertThat(result, hasItem(newWeather))
    }

    @Test
    fun getCurrentWeather_returnsWeatherData() = runTest {
        // When: fetching current weather
        val result = weatherRepository.getCurrentWeather(12.34, 56.78, "en", "metric").toList()

        // Then: assert that the emitted data matches the expected fake response
        assertThat(result[0], `is`(fakeWeatherResponse))
    }

}