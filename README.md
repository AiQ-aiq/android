# 天气预报 App  
《第一行代码：Android》期末大作业

这是一个基于现代 Android 开发技术栈的天气预报应用，采用 **Kotlin + Jetpack Compose + MVVM** 架构，集成和风天气（QWeather）API，实现实时天气、7天预报、空气质量、城市搜索等核心功能。

## 项目亮点

- 完全使用 **Jetpack Compose** 构建声明式 UI  
- 采用 **MVVM + StateFlow** 的现代状态管理  
- 支持动态天气背景（晴/雨/夜/雾等渐变效果）  
- 城市选择持久化存储（DataStore）  
- 优雅的网络异常处理 & 加载动画  
- 下拉刷新 + 友好提示  

## 技术栈

| 分类         | 技术/库                              | 用途                          |
|--------------|--------------------------------------|-------------------------------|
| 语言         | Kotlin                              | 主开发语言                    |
| UI 框架      | Jetpack Compose                     | 声明式界面构建                |
| 架构         | MVVM + StateFlow / MutableStateFlow | 数据与 UI 单向流动            |
| 网络请求     | Retrofit + Moshi Converter          | API 请求 & JSON 解析          |
| 异步处理     | Kotlin Coroutines + Flow            | 协程网络请求 & 状态流         |
| 数据持久化   | Preferences DataStore               | 保存用户选择的城市            |
| 其他         | Accompanist (SwipeRefresh 等)       | 下拉刷新、系统 UI 控制器      |
| 数据源       | 和风天气 QWeather API               | 实时天气 + 预报 + 生活指数    |

## 项目结构
app
├── data
│   ├── model               # 数据类（WeatherResponse, Daily, Hourly, Air 等）
│   ├── network             # Retrofit 接口定义 + OkHttpClient 配置
│   └── repository          # WeatherRepository（数据源统一入口）
├── ui
│   ├── screen
│   │   ├── home            # 主页（实时天气 + 7天预报）
│   │   └── cities          # 城市搜索与选择页面
│   ├── component           # 可复用组件（WeatherCard、IndexItem、Loading 等）
│   └── theme               # 主题、颜色、形状、Typography
├── viewmodel
│   └── WeatherViewModel.kt
├── util
│   └── StorageManager.kt   # DataStore 封装（保存/读取城市）
└── MainActivity.kt
