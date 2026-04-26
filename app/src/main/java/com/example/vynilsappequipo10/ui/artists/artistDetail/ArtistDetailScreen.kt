package com.example.vynilsappequipo10.ui.artists.artistDetail

import androidx.compose.ui.platform.testTag
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: Int,
    artistType: ArtistType,
    onBackClick: () -> Unit,
    onAlbumClick: (Int) -> Unit = {},
    viewModel: ArtistDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadArtist(artistId, artistType)
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = ColorOrangePrimary)
                }
                uiState.error != null -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${uiState.error}", color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadArtist(artistId, artistType) },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                        ) { Text("Reintentar") }
                    }
                }
                uiState.artist != null -> ArtistDetailContent(uiState.artist!!, onAlbumClick)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ArtistDetailContent(artist: Artist, onAlbumClick: (Int) -> Unit) {
    val context = LocalContext.current
    Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 32.dp)
        .testTag("artist_detail_list")
    ){
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.85f)) {
            GlideImage(
                model = GlideUrl(artist.image, LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
                    .build()),
                contentDescription = artist.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) {
                it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
                  .error(ColorDrawable(0xFF2E2824.toInt()))
            }
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(colors = listOf(Color.Transparent, ColorBackground), startY = 300f)
                )
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 20.dp, vertical = 16.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ColorOrangePrimary.copy(alpha = 0.9f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (artist.type == ArtistType.BAND) "BANDA LEGENDARIA" else "ARTISTA LEGENDARIO",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(text = artist.name, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold, lineHeight = 44.sp)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            if (artist.date != null) {
                Spacer(Modifier.height(16.dp))
                val dateLabel = if (artist.type == ArtistType.BAND) "FECHA DE CREACION" else "FECHA DE NACIMIENTO"
                Text(text = dateLabel, color = ColorTextHint, fontSize = 10.sp, letterSpacing = 1.sp)
                Text(text = artist.date.take(10), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))
            Text(text = artist.description, color = Color.White, fontSize = 14.sp, lineHeight = 22.sp)

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "REPRODUCIR RADIO DEL ARTISTA",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorSurface),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(text = "SEGUIR", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }

        if (artist.albums.isNotEmpty()) {
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Albums del\nArtista",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
                Text(
                    text = "VER TODA LA\nCOLECCION",
                    color = ColorOrangePrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                artist.albums.forEach { album -> ArtistAlbumCard(album, onAlbumClick) }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ArtistAlbumCard(album: Album, onAlbumClick: (Int) -> Unit) {
    Column(modifier = Modifier.width(140.dp).clickable { onAlbumClick(album.id) }) {
        GlideImage(
            model = album.cover,
            contentDescription = album.name,
            modifier = Modifier.size(140.dp).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        ) {
            it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
              .error(ColorDrawable(0xFF2E2824.toInt()))
        }
        Spacer(Modifier.height(8.dp))
        Text(text = album.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = "${album.releaseDate.take(4)} - ${album.genre}", color = ColorTextHint, fontSize = 11.sp, maxLines = 1)
    }
}
