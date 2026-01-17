package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.weatherapp.ui.WeatherViewModel
import com.example.weatherapp.ui.screens.CitiesScreen
import com.example.weatherapp.ui.screens.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageLoader = ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()

        Coil.setImageLoader(imageLoader)

        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel()

    // 移除 Scaffold 和 BottomBar，直接使用 NavHost
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                // 传入跳转到城市页面的回调
                onCitySelectClick = {
                    navController.navigate("cities")
                }
            )
        }
        composable("cities") {
            CitiesScreen(viewModel, navController)
        }
    }
}