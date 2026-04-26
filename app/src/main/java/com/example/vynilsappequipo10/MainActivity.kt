package com.example.vynilsappequipo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vynilsappequipo10.ui.main.MainScreen
import com.example.vynilsappequipo10.ui.theme.VynilsTheme
import com.example.vynilsappequipo10.ui.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilsTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") {
                        WelcomeScreen(onContinue = {
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        })
                    }
                    composable("home") {
                        MainScreen()
                    }
                }
            }
        }
    }
}
