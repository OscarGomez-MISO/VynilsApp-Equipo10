package com.example.vynilsappequipo10.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.example.vynilsappequipo10.ui.albums.AlbumsScreen
import com.example.vynilsappequipo10.ui.artists.ArtistsScreen
import com.example.vynilsappequipo10.ui.collectors.CollectorsScreen
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

private enum class MainTab(val label: String, val icon: ImageVector) {
    ALBUMS("ÁLBUMES", Icons.AutoMirrored.Filled.List),
    ARTISTS("ARTISTAS", Icons.Default.Person),
    COLLECTORS("COLECCIONISTAS", Icons.Default.AccountBox)
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(MainTab.ALBUMS) }

    Scaffold(
        containerColor = ColorBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = ColorOrangePrimary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = ColorSurface) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.label)
                        },
                        label = {
                            Text(text = tab.label, fontSize = 9.sp, letterSpacing = 0.3.sp)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ColorOrangePrimary,
                            selectedTextColor = ColorOrangePrimary,
                            indicatorColor = ColorOrangePrimary.copy(alpha = 0.15f),
                            unselectedIconColor = ColorTextHint,
                            unselectedTextColor = ColorTextHint
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            MainTab.ALBUMS     -> AlbumsScreen(modifier = Modifier.padding(innerPadding))
            MainTab.ARTISTS    -> ArtistsScreen(modifier = Modifier.padding(innerPadding))
            MainTab.COLLECTORS -> CollectorsScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
