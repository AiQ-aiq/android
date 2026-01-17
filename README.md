

```markdown
# 天气预报 App（基于 Jetpack Compose + Kotlin + MVVM）

<div align="center">
  <img src="https://via.placeholder.com/800x400/2196F3/FFFFFF?text=Weather+App+Screenshot" alt="App Screenshot" width="60%"/>
  <br/><br/>
  <strong>课程：《第一行代码：Android》期末大作业</strong><br/>
  现代 Android 开发技术栈完整实现
</div>

## 项目简介

这是一个**简洁、美观、功能完整的天气预报应用**，采用当前 Android 官方推荐的技术栈开发：

- **语言**：Kotlin  
- **UI 框架**：Jetpack Compose（完全声明式 UI）  
- **架构**：MVVM + 单向数据流  
- **网络**：Retrofit + Moshi  
- **异步**：Kotlin Coroutines + Flow  
- **数据持久化**：Preferences DataStore  
- **API**：和风天气（QWeather）免费版  

实现功能包括：
- 实时天气查询
- 7天天气预报
- 空气质量指数（AQI）
- 生活指数（穿衣、紫外线、感冒等）
- 城市搜索 + 定位城市
- 城市选择持久化
- 动态天气背景（晴/雨/夜等）
- 网络异常友好提示 & 下拉刷新

## 技术栈

| 分类          | 技术/库                              | 用途                        |
|---------------|--------------------------------------|-----------------------------|
| 语言          | Kotlin                              | 主开发语言                  |
| UI            | Jetpack Compose                     | 声明式 UI 构建              |
| 架构          | MVVM + StateFlow                    | 数据与 UI 分离              |
| 网络请求      | Retrofit + Moshi Converter          | API 请求 & JSON 解析        |
| 协程          | Kotlin Coroutines + Flow            | 异步、网络请求处理          |
| 依赖注入      | Hilt（可选）或手动单例              | 依赖管理                    |
| 数据存储      | Preferences DataStore               | 保存用户选择的城市          |
| 其他          | Accompanist（SwipeRefresh、SystemUiController） | 下拉刷新、状态栏适配        |
| API           | 和风天气 QWeather                   | 天气数据源                  |

## 项目结构

```
app/
├── data/
│   ├── model/              # 数据类（WeatherResponse, Daily, Hourly 等）
│   ├── network/            # Retrofit ApiService + NetworkClient
│   └── repository/         # 数据仓库（WeatherRepository）
├── ui/
│   ├── screen/
│   │   ├── home/           # 主页（实时天气 + 预报）
│   │   └── cities/         # 城市搜索页
│   ├── component/          # 可复用组件（WeatherCard, IndexItem 等）
│   └── theme/              # Compose 主题、颜色、形状
├── viewmodel/
│   └── WeatherViewModel.kt
├── util/
│   └── StorageManager.kt   # DataStore 封装
└── MainActivity.kt
```

## 主要功能截图（建议自己替换为真实截图）

| 实时天气主页 | 7天预报 | 城市搜索 |
|--------------|---------|----------|
| ![主页](screenshots/home_light.jpg) | ![预报](screenshots/forecast.jpg) | ![搜索](screenshots/search.jpg) |

> 温馨提示：建议在 `screenshots/` 文件夹中放入 3~5 张真实应用截图，并修改上面的图片链接。

## 如何运行

1. 克隆项目
   ```bash
   git clone https://github.com/你的用户名/WeatherApp-Compose.git
   ```

2. 替换 API Key  
   在 `local.properties` 或 `gradle.properties` 中添加：
   ```
   QWEATHER_API_KEY=你的和风天气KEY
   ```

3. 打开 Android Studio → Sync Project with Gradle Files

4. 运行（推荐真机或 API 30+ 模拟器）

## 学习收获 & 亮点

- 完全使用 **Jetpack Compose** 实现响应式 UI
- 掌握 **StateFlow + CollectAsState** 的现代状态管理方式
- 实现动态背景渐变（根据白天/夜晚、天气类型）
- 优雅处理网络异常、加载状态、空状态
- 使用 **DataStore** 替代 SharedPreferences 进行数据持久化
- 组件化思想：天气卡片、生活指数等都拆分成独立 Composable

## 致谢

- 《第一行代码 第4版》（郭霖）
- 和风天气 QWeather 开放平台
- Jetpack Compose 官方文档 & Accompanist 库

**欢迎 Star & Fork** 🌟  
如果对 Compose + MVVM 感兴趣，欢迎交流与指正！

最后更新：2026年1月
```

### 使用建议

1. 把上面内容完整复制到你的 `README.md`
2. 在项目根目录创建 `screenshots/` 文件夹，放几张真实运行截图（最少 3 张：主页、预报、搜索）
3. 把 `![主页](screenshots/home_light.jpg)` 改成你真实的图片路径
4. 如果你还没申请和风天气 Key，可以在 README 中加一句“请自行注册和风天气开发者账号获取免费 KEY”
5. 可选：添加 GitHub Actions 徽章、下载量徽章等（如果想让项目更专业）

祝你期末作业顺利拿高分！🚀
