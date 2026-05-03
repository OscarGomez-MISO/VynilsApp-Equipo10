package com.example.vynilsappequipo10.ui.collectors.collectorDetail

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vynilsappequipo10.domain.CollectorAlbumWithAlbum
import com.example.vynilsappequipo10.domain.CollectorDetail
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorDetailScreen(
    collectorId: Int,
    onBackClick: () -> Unit,
    onAlbumClick: (Int) -> Unit = {},
    viewModel: CollectorDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(collectorId) {
        viewModel.loadCollector(collectorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vinilos", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)
            )
        },
        containerColor = ColorBackground
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ColorOrangePrimary
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${uiState.error}", color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadCollector(collectorId) },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                        ) { Text("Reintentar") }
                    }
                }
                uiState.collector != null -> {
                    CollectorDetailContent(
                        collector = uiState.collector!!,
                        collectorAlbums = uiState.collectorAlbums,
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CollectorDetailContent(
    collector: CollectorDetail,
    collectorAlbums: List<CollectorAlbumWithAlbum>,
    onAlbumClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
            .testTag("collector_detail_content")
    ) {
        CollectorHeroSection(collector)

        Spacer(Modifier.height(24.dp))

        CollectorStatsRow(
            albumCount = collectorAlbums.size,
            artistCount = collector.favoritePerformers.size,
            commentCount = collector.comments.size,
            avgRating = if (collector.comments.isEmpty()) null
                        else collector.comments.map { it.rating }.average()
        )

        if (collectorAlbums.isNotEmpty()) {
            Spacer(Modifier.height(28.dp))
            CollectionSection(collectorAlbums, onAlbumClick)
        }
    }
}

@Composable
private fun CollectorHeroSection(collector: CollectorDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(ColorOrangePrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = collector.name.split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                    .ifEmpty { "?" },
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = collector.name,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = collector.email,
            color = ColorTextHint,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = collector.telephone,
            color = ColorTextHint,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun CollectorStatsRow(
    albumCount: Int,
    artistCount: Int,
    commentCount: Int,
    avgRating: Double?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatBox(value = albumCount.toString(), label = "ÁLBUMES", modifier = Modifier.weight(1f))
        StatBox(value = artistCount.toString(), label = "ARTISTAS", modifier = Modifier.weight(1f))
        StatBox(value = commentCount.toString(), label = "RESEÑAS", modifier = Modifier.weight(1f))
        StatBox(
            value = if (avgRating != null) "${(avgRating * 20).roundToInt()}%" else "--",
            label = "RATING",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatBox(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ColorSurface)
            .padding(vertical = 14.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(2.dp))
        Text(text = label, color = ColorTextHint, fontSize = 9.sp, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun CollectionSection(
    albums: List<CollectorAlbumWithAlbum>,
    onAlbumClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Colección",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "VER TODOS",
                color = ColorOrangePrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        val rows = albums.chunked(2)
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { collectorAlbum ->
                    AlbumCard(
                        collectorAlbum = collectorAlbum,
                        onAlbumClick = onAlbumClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AlbumCard(
    collectorAlbum: CollectorAlbumWithAlbum,
    onAlbumClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onAlbumClick(collectorAlbum.album.id) }
    ) {
        GlideImage(
            model = collectorAlbum.album.cover,
            contentDescription = collectorAlbum.album.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        ) {
            it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
              .error(ColorDrawable(0xFF2E2824.toInt()))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = collectorAlbum.album.name,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = collectorAlbum.album.genre,
            color = ColorTextHint,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}
