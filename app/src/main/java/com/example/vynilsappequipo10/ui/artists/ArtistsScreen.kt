package com.example.vynilsappequipo10.ui.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vynilsappequipo10.ui.theme.ColorBackground

@Composable
fun ArtistsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(ColorBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Artistas",
            style = MaterialTheme.typography.headlineMedium,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}
