package com.example.weatherproject.view_model.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Coord
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.Sys
import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.model.local.FakeLocalDataSource
import com.example.weatherproject.model.repo.FakeRepo
import com.example.weatherproject.model.shared_preferences.FakeSharedDataSource
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.remote.FakeRemoteDataSource
import getOrAwaitValue
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertEquals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class FavFragmentViewModelTest{
@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavFragmentViewModel
    private lateinit var Repository: FakeRepo
    val testDispatcher = StandardTestDispatcher()
    val testScope = TestScope(testDispatcher)
    @Before
    fun setUp() {

        Dispatchers.setMain(Dispatchers.Unconfined) // Set the Main dispatcher for tests
        Repository = FakeRepo()
        viewModel = FavFragmentViewModel(Repository)
    }

    @Test
    fun insertWeather_addsWeatherToList() = runTest {
        // Act
        viewModel.insertWeather(48.8566, 2.3522, "Paris")
        viewModel.getAllLocalWeather()
        val weatherList = viewModel.weather
        // Assert
        assertThat(weatherList,not(emptyList<WeatherModel>()))
    }
    @Test
    fun deleteWeather_removeWeatherFromList() = runTest {
        viewModel.insertWeather(48.8566, 2.3522, "Paris")
        var data = WeatherModel("Paris",48.8566, 2.3522,"","")
        viewModel.deleteWeather(data)
        viewModel.getAllLocalWeather()
        val weatherList = viewModel.weather.value
        assertFalse(weatherList?.contains(data) == true)
    }

}