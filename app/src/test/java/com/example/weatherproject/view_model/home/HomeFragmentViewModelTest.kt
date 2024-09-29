package com.example.weatherproject.view_model.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherproject.model.repo.FakeRepo
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.ApiStateForcast
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentViewModelTest{
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var Repository: FakeRepo
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setup(){
         Repository = FakeRepo()
         viewModel = HomeFragmentViewModel(Repository)
    }
    @Test
    fun getCurrentWeather_EmitSuccessWhenDataFetchedSuccessfully() = runTest {
        // Act: Call getCurrentWeather in the ViewModel
        viewModel.getCurrentWeather(48.8566, 2.3522, "en", "metric")
        val weatherState = viewModel.weatherStateFlow.value
        assertTrue(weatherState is ApiState.Success)
        val weatherData = (weatherState as ApiState.Success).data
//        assertEquals(Repository.fakeWeatherResponse, weatherData)
    }
    @Test
    fun getCurrentWeather_emitFailureWhenThereAnError() = runTest {
        Repository.apply { setShouldReturnError(true) }

        viewModel.getCurrentWeather(48.8566, 2.3522, "en", "metric")
        val weatherState = viewModel.weatherStateFlow.value
        assertTrue(weatherState is ApiState.Failure)
        val error = (weatherState as ApiState.Failure).msg
        assertEquals("Error fetching weather", error.message)
    }

}