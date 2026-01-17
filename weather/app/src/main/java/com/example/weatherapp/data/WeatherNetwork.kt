package com.example.weatherapp.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- 常量配置 ---
const val API_KEY = "17dad8bdd6af449487d5ac78d49f4edb"
const val BASE_URL = "https://mc487rqqf7.re.qweatherapi.com"

// --- 接口定义 ---
interface ApiService {
    // 城市搜索
    @GET("/geo/v2/city/lookup")
    suspend fun searchCity(
        @Query("location") query: String,
        @Query("key") key: String = API_KEY
    ): CitySearchResponse

    // 实况天气
    @GET("/v7/weather/now")
    suspend fun getCurrentWeather(
        @Query("location") locationId: String,
        @Query("key") key: String = API_KEY
    ): WeatherResponse

    // 7天天气预报
    @GET("/v7/weather/7d")
    suspend fun getDailyForecast(
        @Query("location") locationId: String,
        @Query("key") key: String = API_KEY
    ): DailyResponse

    // 天气指数
    // type参数说明：
    // 0: 全部指数
    // 1: 运动指数  2: 洗车指数  3: 穿衣指数  4: 钓鱼指数  5: 紫外线指数
    // 6: 旅游指数  7: 花粉过敏指数  8: 舒适度指数  9: 感冒指数
    // 10: 空气污染扩散条件指数  11: 空调开启指数  12: 太阳镜指数
    // 13: 化妆指数  14: 晾晒指数  15: 交通指数  16: 防晒指数
    @GET("/v7/indices/1d")
    suspend fun getIndices(
        @Query("location") locationId: String,
        @Query("type") type: String = "0",  // 默认获取全部指数
        @Query("key") key: String = API_KEY
    ): IndicesResponse

    // 空气质量实况
    @GET("/v7/air/now")
    suspend fun getAirQuality(
        @Query("location") locationId: String,
        @Query("key") key: String = API_KEY
    ): AirQualityResponse
}

// --- 仓库单例 ---
// 使用 Kotlin 的 object 创建单例模式，保证整个应用只有一个 WeatherRepository 实例
object WeatherRepository {

    // 创建 Moshi 实例，用于 JSON 与 Kotlin 对象互转
    private val moshi = Moshi.Builder()
        // 添加对 Kotlin 数据类的支持
        .add(KotlinJsonAdapterFactory())
        .build()

    // 创建 Retrofit 实例，用于网络请求
    private val retrofit = Retrofit.Builder()
        // 设置基础 URL
        .baseUrl(BASE_URL)
        // 添加 Moshi 作为 JSON 转换器
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // 创建 ApiService 接口实例，用于调用网络接口
    val api: ApiService = retrofit.create(ApiService::class.java)
}