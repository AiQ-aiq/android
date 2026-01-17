package com.example.weatherapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    // ---- 配置默认城市 ----
    // 只有在用户第一次安装APP，没有任何历史记录时，才会用这个
    companion object {
        private val DEFAULT_CITY = Location(
            name = "漳州",
            id = "101230601",
            adm2 = "漳州",
            country = "中国"
        )
    }

    private val storage = StorageManager(application)

    private val _weatherState = MutableStateFlow<UiState>(UiState.Empty)
    val weatherState: StateFlow<UiState> = _weatherState

    // 界面观察用的当前城市流
    val currentCity = storage.currentCityFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    val favorites = storage.favoritesFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val searchQuery = MutableStateFlow("")
    val searchResults = MutableStateFlow<List<Location>>(emptyList())

    init {
        // 1. 专门处理“请求天气”的逻辑
        // 只要 currentCity 发生变化（读取到了存盘的城市，或者用户选了新城市），就刷新天气
        viewModelScope.launch {
            currentCity.collect { city ->
                if (city != null) {
                    fetchWeather(city.id)
                }
                // 注意：这里删除了 else 分支，绝对不要在这里设置默认城市！
                // 否则刚启动时的瞬间 null 状态会导致重置。
            }
        }

        // 2. 专门处理“初始化默认城市”的逻辑
        // 只在 ViewModel 创建时执行一次检查
        viewModelScope.launch {
            // 从 Storage 中获取第一个发出的值（即当前存盘的数据）
            val savedCity = storage.currentCityFlow.firstOrNull()

            // 只有当本地完全没有数据时（首次安装），才写入默认城市
            if (savedCity == null) {
                initializeDefaultCity()
            }
            // 如果 savedCity 不为 null（用户之前选过北京），这里什么都不做
            // 上面的 collect 监听器会自动接收到那个“北京”，然后请求天气
        }
    }

    private fun initializeDefaultCity() {
        viewModelScope.launch {
            storage.saveCurrentCity(DEFAULT_CITY)
            addToFavorites(DEFAULT_CITY)
        }
    }

    fun fetchWeather(locationId: String) {
        viewModelScope.launch {
            _weatherState.value = UiState.Loading
            try {
                val currentWeather = WeatherRepository.api.getCurrentWeather(locationId)
                val dailyForecast = WeatherRepository.api.getDailyForecast(locationId)

                val airQuality = try {
                    val resp = WeatherRepository.api.getAirQuality(locationId)
                    if (resp.code == "200") resp.now else null
                } catch (e: Exception) { null }

                val indices = try {
                    val resp = WeatherRepository.api.getIndices(locationId, type = "0")
                    if (resp.code == "200") resp.daily else null
                } catch (e: Exception) { null }

                if (currentWeather.code == "200" && dailyForecast.code == "200") {
                    _weatherState.value = UiState.Success(
                        current = currentWeather.now!!,
                        daily = dailyForecast.daily!!,
                        airQuality = airQuality,
                        indices = indices
                    )
                } else {
                    _weatherState.value = UiState.Error("获取天气失败")
                }
            } catch (e: Exception) {
                _weatherState.value = UiState.Error("网络连接失败")
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                val res = WeatherRepository.api.searchCity(query)
                if (res.code == "200") {
                    searchResults.value = res.location ?: emptyList()
                }
            } catch (_: Exception) { }
        }
    }

    fun clearSearchResults() {
        searchResults.value = emptyList()
    }

    fun selectCity(city: Location) {
        viewModelScope.launch {
            storage.saveCurrentCity(city)
            addToFavorites(city)
            searchQuery.value = ""
            searchResults.value = emptyList()
        }
    }

    private suspend fun addToFavorites(city: Location) {
        val currentList = favorites.value.toMutableList()
        if (currentList.none { it.id == city.id }) {
            currentList.add(0, city)
            storage.saveFavorites(currentList)
        }
    }

    fun removeFavorite(city: Location) {
        viewModelScope.launch {
            val currentList = favorites.value.filter { it.id != city.id }
            storage.saveFavorites(currentList)
        }
    }
}