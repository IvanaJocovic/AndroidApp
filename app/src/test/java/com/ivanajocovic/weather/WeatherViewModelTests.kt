package com.ivanajocovic.weather

import com.ivanajocovic.weather.networking.datasource.WeatherDataSource
import com.ivanajocovic.weather.usecase.TransformWeatherResponseToWeatherUiUseCase
import com.ivanajocovic.weather.viewmodel.WeatherViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
internal class WeatherViewModelTests {

    val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    var coroutineTestRule: TestRule = object : TestWatcher() {
        override fun starting(description: Description) {
            super.starting(description)
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            super.finished(description)
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

    private lateinit var viewModel: WeatherViewModel

    private val dataSource = mock<WeatherDataSource>()
    private val useCase = mock<TransformWeatherResponseToWeatherUiUseCase>()

    @Before
    fun setUp() {
        viewModel = WeatherViewModel(
            dataSource,
            useCase
        )
    }

    @Test
    fun `test getWeatherInfo, expecting happy flow`() = runTest {

        //Given

        val states = mutableListOf<WeatherViewModel.WeatherUiState>()
        val expectedUiModel = getWeatherUiModel()
        whenever(
            dataSource.getForecast(
                latitude = any(),
                longitude = any(),
                hourly = any(),
                daily = any(),
                timezone = any()
            )
        ).thenReturn(getWeatherResponse())

        whenever(
            useCase.invoke(response = any())
        ).thenReturn(expectedUiModel)

        viewModel.uiState
            .onEach(states::add)
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        //When

        viewModel.getWeatherInfo()
        runCurrent()

        //Then
        assertEquals(
            listOf(
                WeatherViewModel.WeatherUiState.NoContent,
                WeatherViewModel.WeatherUiState.Loading,
                WeatherViewModel.WeatherUiState.Success(expectedUiModel)
            ),
            states
        )
    }

    @Test
    fun `test getWeatherInfo, expecting error`() = runTest {

        //Given

        val states = mutableListOf<WeatherViewModel.WeatherUiState>()
        val expectedUiModel = getWeatherUiModel()
        val expectedError = NullPointerException("Something went wrong")

        whenever(
            dataSource.getForecast(
                latitude = any(),
                longitude = any(),
                hourly = any(),
                daily = any(),
                timezone = any()
            )
        ).thenThrow(expectedError)

        whenever(
            useCase.invoke(response = any())
        ).thenReturn(expectedUiModel)

        viewModel.uiState
            .onEach(states::add)
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        //When

        viewModel.getWeatherInfo()
        runCurrent()

        //Then
        assertEquals(
            listOf(
                WeatherViewModel.WeatherUiState.NoContent,
                WeatherViewModel.WeatherUiState.Loading,
                WeatherViewModel.WeatherUiState.Error(expectedError)
            ),
            states
        )
    }
}