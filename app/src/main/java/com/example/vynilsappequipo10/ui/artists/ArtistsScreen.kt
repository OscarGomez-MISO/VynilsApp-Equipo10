package com.example.vynilsappequipo10.ui.artists

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.vynilsappequipo10.domain.Artist
import com.example.vynilsappequipo10.domain.ArtistType
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier,
    onArtistClick: (Int, ArtistType) -> Unit = { _, _ -> },
    viewModel: ArtistsViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(ColorBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ColorOrangePrimary)
            }
        }

        uiState.error != null -> {

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(ColorBackground),
                contentAlignment = Alignment.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "No se pudieron cargar los artistas",
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = uiState.error!!,
                        color = ColorTextHint,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadArtists() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorOrangePrimary
                        )
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }

        else -> {

            ArtistsContent(
                modifier = modifier,
                uiState = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onArtistClick = onArtistClick
            )
        }
    }
}

@Composable
private fun ArtistsContent(
    modifier: Modifier,
    uiState: ArtistsUiState,
    onSearchQueryChange: (String) -> Unit,
    onArtistClick: (Int, ArtistType) -> Unit
) {

    LazyColumn(
     modifier = Modifier
        .fillMaxSize()
        .background(ColorBackground)
        .testTag("artist_detail_list"),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
    ) {

        item {

            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {

                Text(
                    text = "BIBLIOTECA CURADA",
                    color = ColorOrangePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Artistas",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Un índice editorial de tu paisaje sonoro. ${uiState.artists.size} visionarios ordenados por impacto y frecuencia.",
                    color = ColorTextHint,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        item {

            ArtistsSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }

        items(uiState.artists, key = { "artist_${it.id}" }) { artist ->

            ArtistRow(
                artist = artist,
                onClick = {
                    onArtistClick(
                        artist.id,
                        artist.type
                    )
                }
            )
        }

        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ArtistsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ColorSurface)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = ColorTextHint,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {

                if (query.isEmpty()) {

                    Text(
                        text = "Busca en las cajas...",
                        color = ColorTextHint,
                        fontSize = 14.sp
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(ColorOrangePrimary),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("artist_search_field")
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ArtistRow(
    artist: Artist,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorSurface)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        GlideImage(
            model = GlideUrl(
                artist.image,
                LazyHeaders.Builder()
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36"
                    )
                    .build()
            ),
            contentDescription = artist.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        ) {

            it.placeholder(ColorDrawable(0xFF2E2824.toInt()))
                .error(ColorDrawable(0xFF2E2824.toInt()))
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = artist.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            if (artist.date != null) {

                Text(
                    text = artist.date.take(10),
                    color = ColorTextHint,
                    fontSize = 12.sp
                )
            }

            Text(
                text = if (artist.type == ArtistType.BAND) "BANDA" else "MÚSICO",
                color = ColorTextHint,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            )
        }

        if (artist.albums.isNotEmpty()) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(ColorOrangePrimary.copy(alpha = 0.15f))
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) {

                Text(
                    text = "${artist.albums.size.toString().padStart(2, '0')} ÁLBUMES",
                    color = ColorOrangePrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}