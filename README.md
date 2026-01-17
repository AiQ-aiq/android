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
