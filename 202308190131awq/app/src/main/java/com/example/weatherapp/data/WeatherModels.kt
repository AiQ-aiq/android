package com.example.weatherapp.data

import com.squareup.moshi.JsonClass

// 城市搜索响应
@JsonClass(generateAdapter = true)
data class CitySearchResponse(
    val code: String,
    val location: List<Location>? = null
)

@JsonClass(generateAdapter = true)
data class Location(
    val name: String,
    val id: String,
    val adm2: String? = null,
    val country: String? = null
)

// 实况天气响应
@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val code: String,
    val now: NowWeather? = null
)

@JsonClass(generateAdapter = true)
data class NowWeather(
    val temp: String,
    val feelsLike: String,
    val icon: String,
    val text: String,
    val windDir: String,
    val windScale: String,
    val humidity: String,
    val pressure: String,
    val vis: String
)

// 每日天气响应
@JsonClass(generateAdapter = true)
data class DailyResponse(
    val code: String,
    val daily: List<Daily>? = null
)

@JsonClass(generateAdapter = true)
data class Daily(
    val fxDate: String,
    val tempMax: String,
    val tempMin: String,
    val iconDay: String,
    val textDay: String,
    val sunrise: String? = null,
    val sunset: String? = null
)

// 天气指数响应
@JsonClass(generateAdapter = true)
data class IndicesResponse(
    val code: String,
    val daily: List<IndicesDaily>? = null
)

@JsonClass(generateAdapter = true)
data class IndicesDaily(
    val date: String,           // 预报日期
    val type: String,           // 生活指数类型ID
    val name: String,           // 生活指数类型的名称
    val level: String,          // 生活指数预报等级
    val category: String,       // 生活指数预报级别名称
    val text: String            // 生活指数预报的详细描述
)

// 空气质量响应
@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    val code: String,
    val now: AirQualityNow? = null
)

@JsonClass(generateAdapter = true)
data class AirQualityNow(
    val pubTime: String,        // 空气质量数据发布时间
    val aqi: String,            // 空气质量指数
    val level: String,          // 空气质量指数等级
    val category: String,       // 空气质量指数级别
    val primary: String,        // 空气质量的主要污染物，优时返回NA
    val pm10: String,           // PM10
    val pm2p5: String,          // PM2.5
    val no2: String,            // 二氧化氮
    val so2: String,            // 二氧化硫
    val co: String,             // 一氧化碳
    val o3: String              // 臭氧
)