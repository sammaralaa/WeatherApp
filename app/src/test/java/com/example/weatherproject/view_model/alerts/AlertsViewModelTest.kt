package com.example.weatherproject.view_model.alerts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.repo.FakeRepo
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertsViewModelTest{
    private lateinit var viewModel: AlertsViewModel
    private lateinit var Repository: FakeRepo
    var a = AlarmData(5,45222222,45555,"n","hgh","frdr")
    var a2 = AlarmData(5,45222222,45555,"n","hgh","frdr")
    var a3 = AlarmData(5,45222222,45555,"n","hgh","frdr")

    @Before
    fun setUp() {

        Dispatchers.setMain(Dispatchers.Unconfined) // Set the Main dispatcher for tests
        Repository = FakeRepo()
        viewModel = AlertsViewModel(Repository)

    }
    @Test
    fun insertWeather_addsWeatherToList() = runTest {
        // Act
        viewModel.insertAlert(a)
        viewModel.getAllAlerts()
        val weatherList = viewModel.AlertItem
        // Assert
        assertThat(weatherList, not(emptyList<AlarmData>()))
    }
//    @Test
//    fun insertWeather_returnEmpty() = runTest {
//        // Act
//       // viewModel.insertAlert(a)
//        viewModel.getAllAlerts()
//        val weatherList = viewModel.AlertItem.value
//        // Assert
//        assertThat(weatherList, `is`(emptyList<AlarmData>()))
//    }
}