package com.example.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.UiState
import com.example.weatherapp.ui.WeatherViewModel

/**
 * 主页屏幕 UI 组合函数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    onCitySelectClick: () -> Unit
) {
    val state by viewModel.weatherState.collectAsState()
    val city by viewModel.currentCity.collectAsState()

    // 背景逻辑
    val bgBrush = when (state) {
        is UiState.Success -> {
            val iconCode = (state as UiState.Success).current.icon.toIntOrNull() ?: 100
            val isNight = iconCode > 150
            if (isNight) {
                Brush.verticalGradient(
                    listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            } else {
                Brush.verticalGradient(
                    listOf(Color(0xFF1e3c72), Color(0xFF2a5298), Color(0xFF7474BF))
                )
            }
        }
        else -> Brush.verticalGradient(listOf(Color(0xFF485563), Color(0xFF29323C)))
    }

    Scaffold(
        containerColor = Color.Transparent,
        // 【修改1】移除了 floatingActionButton
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- 顶部栏修改开始 ---
                if (city != null) {
                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        // 1. 中间的城市信息 (保持不变)
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = city?.name ?: "",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 32.sp
                                    ),
                                    color = Color.White
                                )
                            }
                            Text(
                                text = city?.adm2 ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // 【修改2】右侧按钮组：搜索在上方，刷新在下方
                        Column(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp) // 按钮之间的间距
                        ) {
                            // 搜索按钮
                            IconButton(
                                onClick = onCitySelectClick,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Select City",
                                    tint = Color.White
                                )
                            }

                            // 刷新按钮 (样式与搜索按钮统一)
                            IconButton(
                                onClick = { city?.id?.let { viewModel.fetchWeather(it) } },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
                // --- 顶部栏修改结束 ---

                Spacer(Modifier.height(20.dp))

                // 内容区域
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    when (val s = state) {
                        is UiState.Loading -> Box(
                            Modifier.fillMaxSize().height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is UiState.Error -> Box(
                            Modifier.fillMaxSize().height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                s.message,
                                color = Color.Red,
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.8f))
                                    .padding(8.dp)
                            )
                        }
                        is UiState.Empty -> Text(
                            "暂无数据",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        is UiState.Success -> WeatherContent(
                            now = s.current,
                            daily = s.daily,
                            airQuality = s.airQuality,
                            indices = s.indices
                        )
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}