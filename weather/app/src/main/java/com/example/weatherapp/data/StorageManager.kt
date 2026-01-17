package com.example.weatherapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 通过扩展属性为Context创建DataStore实例，文件名为"weather_prefs"
private val Context.dataStore by preferencesDataStore("weather_prefs")

class StorageManager(private val context: Context) {
    // 初始化Moshi JSON解析器，添加Kotlin反射支持
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // 定义Location列表的泛型类型，用于JSON序列化/反序列化
    private val listType = Types.newParameterizedType(List::class.java, Location::class.java)

    // 创建Location列表的JSON适配器
    private val jsonAdapter = moshi.adapter<List<Location>>(listType)

    // 创建单个Location对象的JSON适配器
    private val locationAdapter = moshi.adapter(Location::class.java)

    // 定义存储收藏城市列表的键
    private val KEY_FAVORITES = stringPreferencesKey("favorites")

    // 定义存储当前选中城市的键
    private val KEY_CURRENT_CITY = stringPreferencesKey("current_city")

    /**
     * 获取当前城市的Flow数据流
     * @return Flow<Location?> 可能为空的Location对象流
     */
    val currentCityFlow: Flow<Location?> = context.dataStore.data.map { prefs ->
        // 从DataStore中获取当前城市的JSON字符串
        prefs[KEY_CURRENT_CITY]?.let { json ->
            try {
                // 将JSON字符串反序列化为Location对象
                locationAdapter.fromJson(json)
            } catch (e: Exception) {
                // 解析失败时返回null
                null
            }
        }
    }

    /**
     * 获取收藏城市列表的Flow数据流
     * @return Flow<List<Location>> Location对象列表流
     */
    val favoritesFlow: Flow<List<Location>> = context.dataStore.data.map { prefs ->
        // 从DataStore中获取收藏列表的JSON字符串
        prefs[KEY_FAVORITES]?.let { json ->
            try {
                // 将JSON字符串反序列化为Location列表
                jsonAdapter.fromJson(json) ?: emptyList()
            } catch (e: Exception) {
                // 解析失败时返回空列表
                emptyList()
            }
        } ?: emptyList() // 如果没有存储数据，返回空列表
    }

    /**
     * 保存当前选中的城市
     * @param city 要保存的Location对象
     */
    suspend fun saveCurrentCity(city: Location) {
        // 将Location对象序列化为JSON字符串
        val json = locationAdapter.toJson(city)
        // 使用edit事务保存到DataStore
        context.dataStore.edit { it[KEY_CURRENT_CITY] = json }
    }

    /**
     * 保存收藏城市列表
     * @param list 要保存的Location对象列表
     */
    suspend fun saveFavorites(list: List<Location>) {
        // 将Location列表序列化为JSON字符串
        val json = jsonAdapter.toJson(list)
        // 使用edit事务保存到DataStore
        context.dataStore.edit { it[KEY_FAVORITES] = json }
    }
}
