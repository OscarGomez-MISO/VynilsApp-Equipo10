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
                        WelcomeScreen(
                            onCollectorEntry = {
                                navController.navigate("home/collector") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            },
                            onVisitorEntry = {
                                navController.navigate("home/visitor") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("home/{userRole}") { backStackEntry ->
                        val userRole = backStackEntry.arguments?.getString("userRole") ?: "visitor"
                        MainScreen(
                            isCollector = userRole == "collector",
                            onLogout = {
                                navController.navigate("welcome") {
                                    popUpTo("home/{userRole}") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
