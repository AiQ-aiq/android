package com.example.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.ui.WeatherViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(viewModel: WeatherViewModel, navController: NavController) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    // 防抖 + 清空逻辑
    LaunchedEffect(query) {
        if (query.length > 1) {
            delay(500)
            viewModel.search(query)
        } else {
            viewModel.clearSearchResults()  // 输入 ≤1 个字符时立即清空搜索结果
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = { viewModel.searchQuery.value = it },
                onSearch = { viewModel.search(query) },
                active = false,
                onActiveChange = {},
                placeholder = { Text("搜索城市，如：北京、上海、Guangzhou") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
                trailingIcon = {
                    AnimatedVisibility(visible = query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "清空搜索")
                        }
                    }
                },
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {}
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (query.isEmpty()) {
                // ==================== 常用城市 ====================
                item {
                    Text(
                        text = "常用城市",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (favorites.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text("暂无常用城市", color = MaterialTheme.colorScheme.outline)
                                Text(
                                    "搜索并选择城市即可自动添加",
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                items(favorites, key = { it.id }) { city ->
                    Card(
                        onClick = {
                            viewModel.selectCity(city)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(city.name, fontWeight = FontWeight.Medium) },
                            supportingContent = {
                                Text(
                                    buildString {
                                        if (!city.adm2.isNullOrBlank()) append(city.adm2)
                                        if (!city.country.isNullOrBlank()) {
                                            if (isNotEmpty()) append(", ")
                                            append(city.country)
                                        }
                                    }
                                )
                            },
                            leadingContent = {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                            },
                            trailingContent = {
                                IconButton(onClick = { viewModel.removeFavorite(city) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "删除常用城市",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                // ==================== 搜索结果 ====================
                item {
                    Text(
                        text = "搜索结果（${results.size}）",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (results.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("未找到匹配的城市", color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

                items(results, key = { it.id }) { city ->
                    Card(
                        onClick = {
                            viewModel.selectCity(city)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(city.name, fontWeight = FontWeight.Medium) },
                            supportingContent = {
                                Text(
                                    buildString {
                                        if (!city.adm2.isNullOrBlank()) append(city.adm2)
                                        if (!city.country.isNullOrBlank()) {
                                            if (isNotEmpty()) append(", ")
                                            append(city.country)
                                        }
                                    }
                                )
                            },
                            leadingContent = {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }
    }
}