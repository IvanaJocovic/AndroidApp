package com.ivanajocovic.weather

import com.ivanajocovic.weather.usecase.TransformWeatherResponseToWeatherUiUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class TransformWeatherResponseToWeatherUiUseCaseTest {

    private val useCase = TransformWeatherResponseToWeatherUiUseCase()

    @Test
    fun `test transforming weather response to weather ui model`() {

        //Given
        val weatherResponse = getWeatherResponse()
        val expectedWeatherUiModel = getWeatherUiModel()

        //When

        val resultedWeatherUiModel = useCase(weatherResponse)

        //Then

        assertEquals(
            expectedWeatherUiModel,
            resultedWeatherUiModel
        )
    }
}
