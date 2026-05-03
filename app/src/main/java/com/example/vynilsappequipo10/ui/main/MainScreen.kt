package com.example.vynilsappequipo10.ui.main

import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vynilsappequipo10.ui.albums.AlbumsScreen
import com.example.vynilsappequipo10.ui.albums.albumDetail.AlbumDetailScreen
import com.example.vynilsappequipo10.ui.artists.ArtistsScreen
import com.example.vynilsappequipo10.domain.ArtistType
import com.example.vynilsappequipo10.ui.artists.artistDetail.ArtistDetailScreen
import com.example.vynilsappequipo10.ui.collectors.CollectorsScreen
import com.example.vynilsappequipo10.ui.collectors.collectorDetail.CollectorDetailScreen
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

private enum class MainTab(val route: String, val label: String, val icon: ImageVector) {
    ALBUMS("albums_list", "ÁLBUMES", Icons.AutoMirrored.Filled.List),
    ARTISTS("artists_list", "ARTISTAS", Icons.Default.Person),
    COLLECTORS("collectors_list", "COLECCIONISTAS", Icons.Default.AccountBox)
}

@Composable
fun MainScreen(isCollector: Boolean, onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Scaffold(
        containerColor = ColorBackground,
        floatingActionButton = {
            if (isCollector && (currentRoute == MainTab.ALBUMS.route || currentRoute == MainTab.ARTISTS.route || currentRoute == MainTab.COLLECTORS.route)) {
                FloatingActionButton(
                    onClick = {
                        Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = ColorOrangePrimary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = ColorSurface) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
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
        NavHost(
            navController = navController,
            startDestination = MainTab.ALBUMS.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainTab.ALBUMS.route) {
                AlbumsScreen(
                    onAlbumClick = { albumId ->
                        navController.navigate("album_detail/$albumId")
                    },
                    onLogout = onLogout
                )
            }
            composable(
                route = "album_detail/{albumId}",
                arguments = listOf(navArgument("albumId") { type = NavType.IntType })
            ) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getInt("albumId") ?: return@composable
                AlbumDetailScreen(
                    albumId = albumId,
                    isCollector = isCollector,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(MainTab.ARTISTS.route) {
                ArtistsScreen(
                    onArtistClick = { artistId, artistType ->
                        navController.navigate("artist_detail/$artistId/${artistType.name}")
                    }
                )
            }
            composable(
                route = "artist_detail/{artistId}/{artistType}",
                arguments = listOf(
                    navArgument("artistId") { type = NavType.IntType },
                    navArgument("artistType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val artistId = backStackEntry.arguments?.getInt("artistId") ?: return@composable
                val artistType = ArtistType.valueOf(
                    backStackEntry.arguments?.getString("artistType") ?: ArtistType.MUSICIAN.name
                )
                ArtistDetailScreen(
                    artistId = artistId,
                    artistType = artistType,
                    onBackClick = { navController.popBackStack() },
                    onAlbumClick = { albumId ->
                        navController.navigate("album_detail/$albumId")
                    }
                )
            }
            composable(MainTab.COLLECTORS.route) {
                CollectorsScreen(
                    onCollectorClick = { collectorId ->
                        navController.navigate("collector_detail/$collectorId")
                    }
                )
            }
            composable(
                route = "collector_detail/{collectorId}",
                arguments = listOf(navArgument("collectorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val collectorId = backStackEntry.arguments?.getInt("collectorId") ?: return@composable
                CollectorDetailScreen(
                    collectorId = collectorId,
                    onBackClick = { navController.popBackStack() },
                    onAlbumClick = { albumId ->
                        navController.navigate("album_detail/$albumId")
                    }
                )
            }
        }
    }
}
