package com.example.weatherapp.ui

import com.example.weatherapp.data.*

/**
 * UI 状态封装
 */
sealed class UiState {
    data object Loading : UiState()
    data object Empty : UiState()

    data class Success(
        val current: NowWeather,
        val daily: List<Daily>,
        val airQuality: AirQualityNow? = null,
        val indices: List<IndicesDaily>? = null
    ) : UiState()

    data class Error(val message: String) : UiState()
}
