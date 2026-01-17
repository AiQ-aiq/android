package com.example.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.data.AirQualityNow
import com.example.weatherapp.data.Daily
import com.example.weatherapp.data.IndicesDaily
import com.example.weatherapp.data.NowWeather

/**
 * 天气详情内容主入口
 *
 * 该 Composable 负责展示：
 * - 当前天气（温度、图标、描述）
 * - 气象详情（体感、湿度等）
 * - 空气质量（可选）
 * - 未来多日预报
 * - 生活指数（可选，已移至多日预报下方）
 */
@Composable
fun ColumnScope.WeatherContent(
    now: NowWeather,               // 当前天气数据
    daily: List<Daily>,           // 未来每日预报列表
    airQuality: AirQualityNow?,   // 空气质量（可能为空）
    indices: List<IndicesDaily>?  // 生活指数（可能为空）
) {
    // 1️⃣ 主天气卡片：大图标 + 超大温度 + 天气描述
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 异步加载和风天气图标（使用 Coil 库）
        AsyncImage(
            model = "https://a.hecdn.net/img/common/icon/202106d/${now.icon}.png",
            contentDescription = now.text,  // 图标语义描述（用于无障碍）
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Fit   // 保持比例缩放
        )

        // 当前温度（超大字体，浅色）
        Text(
            text = "${now.temp}°",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 100.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-2).sp  // 字母间距微调，更紧凑美观
            ),
            color = Color.White
        )

        // 天气文字描述（如"晴"、"多云转雨"）
        Text(
            text = now.text,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White.copy(alpha = 0.9f)
        )
    }

    Spacer(Modifier.height(40.dp)) // 分隔间距

    // 2️⃣ 气象详情卡片（毛玻璃效果）
    GlassyCard {
        Row(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // 均匀分布
        ) {
            DetailItem("体感", "${now.feelsLike}°")
            VerticalDivider()
            DetailItem("湿度", "${now.humidity}%")
            VerticalDivider()
            DetailItem("风力", "${now.windScale}级")
            VerticalDivider()
            DetailItem("能见度", "${now.vis}km")
        }
    }

    Spacer(Modifier.height(24.dp))

    // 3️⃣ 空气质量卡片（仅当数据存在时显示）
    if (airQuality != null) {
        AirQualityCard(airQuality)
        Spacer(Modifier.height(24.dp))
    }

    // 4️⃣ 未来预报标题
    Text(
        "未来预报",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
    )
    Spacer(Modifier.height(12.dp))

    // 使用 LazyColumn 高效渲染多日预报（避免一次性加载全部）
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp), // 限制最大高度，防止占满屏幕
        verticalArrangement = Arrangement.spacedBy(12.dp) // 项之间固定间距
    ) {
        items(daily) { day ->
            ForecastItem(day) // 每一项是一个 ForecastItem
        }
    }

    // 5️⃣ 生活指数区域（MOVED HERE - 放在多日预报之后）
    if (!indices.isNullOrEmpty()) {
        Spacer(Modifier.height(24.dp))
        LifeIndicesSection(indices)
        Spacer(Modifier.height(24.dp))
    }
}

// ────────────────────────────────────────────────
// 🎨 通用 UI 组件
// ────────────────────────────────────────────────

/**
 * 毛玻璃风格卡片（半透明 + 圆角 + 白色边框）
 */
@Composable
fun GlassyCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        color = Color.White.copy(alpha = 0.15f), // 半透明白色背景
        shape = RoundedCornerShape(24.dp),       // 圆角
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp)) // 白色半透明边框
    ) {
        content() // 内容由调用方提供
    }
}

/**
 * 垂直分隔线（用于 DetailItem 之间）
 */
@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(30.dp)
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.3f))
    )
}

/**
 * 详情项：上方数值，下方标签
 */
@Composable
fun DetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

// ────────────────────────────────────────────────
// 📅 未来预报项
// ────────────────────────────────────────────────

/**
 * 单日预报卡片
 */
@Composable
fun ForecastItem(day: Daily) {
    GlassyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 日期（如 "12月20日"）
            Text(
                text = day.fxDate,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                modifier = Modifier.weight(1.2f) // 占比略大
            )

            Spacer(Modifier.width(8.dp))

            // 白天天气图标
            AsyncImage(
                model = "https://a.hecdn.net/img/common/icon/202106d/${day.iconDay}.png",
                contentDescription = day.textDay,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.width(12.dp))

            // 白天天气描述
            Text(
                text = day.textDay,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            // 温度范围（如 "2~10°C"）
            Text(
                text = "${day.tempMin}~${day.tempMax}°",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}

// ────────────────────────────────────────────────
// 🌫️ 空气质量卡片
// ────────────────────────────────────────────────

/**
 * 空气质量详情展示
 */
@Composable
fun ColumnScope.AirQualityCard(airQuality: AirQualityNow) {
    // 标题
    Text(
        "空气质量",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
    )
    Spacer(Modifier.height(12.dp))

    GlassyCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // AQI 数值 + 等级标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AQI",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = airQuality.aqi,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                // AQI 等级色块（根据数值变色）
                Surface(
                    color = getAqiColor(airQuality.aqi.toIntOrNull() ?: 0),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = airQuality.category, // 如"优"、"良"
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 主要污染物（如果存在）
            if (airQuality.primary != "NA") {
                Text(
                    text = "主要污染物: ${airQuality.primary}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(16.dp))
            }

            // 各污染物浓度 Grid（2 行 x 3 列）
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    PollutantItem("PM2.5", airQuality.pm2p5, Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    PollutantItem("PM10", airQuality.pm10, Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    PollutantItem("O₃", airQuality.o3, Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    PollutantItem("NO₂", airQuality.no2, Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    PollutantItem("SO₂", airQuality.so2, Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    PollutantItem("CO", airQuality.co, Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * 单个污染物项：名称 + 数值
 */
@Composable
fun PollutantItem(name: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
        Spacer(Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = Color.White)
    }
}

/**
 * 根据 AQI 数值返回对应颜色（国家标准）
 */
fun getAqiColor(aqi: Int): Color {
    return when {
        aqi <= 50 -> Color(0xFF00E400)   // 优（绿色）
        aqi <= 100 -> Color(0xFFFFFF00)  // 良（黄色）
        aqi <= 150 -> Color(0xFFFF7E00)  // 轻度污染（橙色）
        aqi <= 200 -> Color(0xFFFF0000)  // 中度污染（红色）
        aqi <= 300 -> Color(0xFF8F3F97)  // 重度污染（紫色）
        else -> Color(0xFF7E0023)        // 严重污染（深红）
    }
}

// ────────────────────────────────────────────────
// 🧴 生活指数区域
// ────────────────────────────────────────────────

/**
 * 生活指数展示区（网格布局）
 */
@Composable
fun ColumnScope.LifeIndicesSection(indices: List<IndicesDaily>) {
    Text(
        "生活指数",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
    )
    Spacer(Modifier.height(12.dp))

    // 只显示常用指数（按 type 过滤），最多 6 项
    val displayIndices = indices.filter {
        it.type in listOf("1", "2", "3", "5", "8", "9", "16") // 对应：运动、洗车、穿衣、紫外线、心情、感冒、防晒
    }.take(6)

    // 每行 3 个，不足补空 Spacer
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        displayIndices.chunked(3).forEach { rowIndices ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowIndices.forEach { index ->
                    IndicesCard(index, Modifier.weight(1f))
                }
                // 补齐 3 列（避免最后一行错位）
                repeat(3 - rowIndices.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

/**
 * 单个生活指数卡片
 */
@Composable
fun IndicesCard(index: IndicesDaily, modifier: Modifier = Modifier) {
    GlassyCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 使用 emoji 作为图标（轻量、无需网络）
            Text(
                text = getIndicesIcon(index.type),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            // 指数名称（如"紫外线指数"）
            Text(
                text = index.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.White,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            // 建议等级（如"弱"、"强"），带颜色提示
            Text(
                text = index.category,
                style = MaterialTheme.typography.bodySmall,
                color = getIndicesLevelColor(index.level),
                maxLines = 1
            )
        }
    }
}

/**
 * 根据生活指数类型返回对应 emoji 图标
 */
fun getIndicesIcon(type: String): String {
    return when (type) {
        "1" -> "🏃"  // 运动
        "2" -> "🚗"  // 洗车
        "3" -> "👔"  // 穿衣
        "5" -> "☀️"  // 紫外线
        "8" -> "😊"  // 心情
        "9" -> "🤧"  // 感冒
        "16" -> "🧴" // 防晒
        else -> "📋" // 默认
    }
}

/**
 * 根据生活指数等级（1-4）返回颜色
 * 1: 很适宜（绿），2: 适宜（黄），3: 较不适宜（橙），4: 不适宜（红）
 */
fun getIndicesLevelColor(level: String): Color {
    return when (level.toIntOrNull() ?: 3) {
        1 -> Color(0xFF00E400)   // 绿
        2 -> Color(0xFFFFFF00)   // 黄
        3 -> Color(0xFFFF7E00)   // 橙
        else -> Color(0xFFFF0000) // 红
    }
}