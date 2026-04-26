package com.example.vynilsappequipo10.ui.albums.albumDetail

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.Performer
import com.example.vynilsappequipo10.domain.Track
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumId: Int,
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Álbum", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorBackground
                )
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
                        Text(text = "Error: ${uiState.error}", color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadAlbum(albumId) },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.album != null -> {
                    AlbumDetailContent(uiState.album!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AlbumDetailContent(album: Album) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Portada e Información Básica
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GlideImage(
                    model = album.cover,
                    contentDescription = album.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                ) {
                    it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
                      .error(ColorDrawable(0xFF2E2824.toInt()))
                }

                Column {
                    Text(
                        text = album.name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${album.genre} • ${album.recordLabel}",
                        color = ColorOrangePrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Lanzamiento: ${album.releaseDate.take(10)}",
                        color = ColorTextHint,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Descripción
        item {
            SectionTitle("Descripción")
            Text(
                text = album.description,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }

        // Tracks (Canciones)
        if (album.tracks.isNotEmpty()) {
            item { SectionTitle("Canciones") }
            items(album.tracks) { track ->
                TrackItem(track)
            }
        }

        // Performers (Artistas)
        if (album.performers.isNotEmpty()) {
            item { SectionTitle("Artistas") }
            items(album.performers) { performer ->
                PerformerItem(performer)
            }
        }

        // Comments (Comentarios)
        if (album.comments.isNotEmpty()) {
            item { SectionTitle("Comentarios") }
            items(album.comments) { comment ->
                CommentItem(comment)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun TrackItem(track: Track) {
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = track.name,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = track.duration,
                color = ColorTextHint,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PerformerItem(performer: Performer) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = performer.image,
            contentDescription = performer.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        ) {
            it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
              .error(ColorDrawable(0xFF2E2824.toInt()))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = performer.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = performer.description,
                color = ColorTextHint,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < comment.rating) ColorOrangePrimary else Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = comment.description,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
