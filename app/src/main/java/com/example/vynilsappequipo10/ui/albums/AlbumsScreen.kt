package com.example.vynilsappequipo10.ui.albums

import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

import androidx.compose.material.icons.filled.Home

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    onAlbumClick: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: AlbumsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize().background(ColorBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ColorOrangePrimary)
            }
        }
        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize().background(ColorBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No se pudo cargar los álbumes",
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
                        onClick = { viewModel.loadAlbums() },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }
        else -> {
            AlbumsContent(
                modifier = modifier,
                uiState = AlbumsUiState(
                    albums = uiState.albums,
                    searchQuery = uiState.searchQuery
                ),
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onAlbumClick = onAlbumClick,
                onLogout = onLogout
            )
        }
    }
}

@Composable
private fun AlbumsContent(
    modifier: Modifier,
    uiState: AlbumsUiState,
    onSearchQueryChange: (String) -> Unit,
    onAlbumClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().background(ColorBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) { AlbumsHeader(onLogout) }
        item(span = { GridItemSpan(2) }) { StatsSection(total = uiState.albums.size) }
        item(span = { GridItemSpan(2) }) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }
        item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(4.dp)) }
        items(uiState.albums) { album -> AlbumCard(album, onAlbumClick) }
        item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun AlbumsHeader(onLogout: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Vinilos",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        
        // Botón de Inicio (Home) con icono restaurado y semántica para pruebas
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ColorOrangePrimary)
                .clickable { onLogout() }
                .semantics { contentDescription = "Boton Inicio" }
                .testTag("home_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun StatsSection(total: Int) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "$total",
                color = ColorOrangePrimary,
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 52.sp
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "TOTAL DE LPS",
                color = ColorTextHint,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Sonidos curados y ediciones raras de tu bóveda privada.",
            color = ColorTextHint,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(ColorSurface)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = ColorTextHint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(text = "Busca en tus cajas...", color = ColorTextHint, fontSize = 14.sp)
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    cursorBrush = SolidColor(ColorOrangePrimary),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("album_search_field")
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AlbumCard(album: Album, onAlbumClick: (Int) -> Unit) {
    Column(modifier = Modifier.clickable { onAlbumClick(album.id) }) {
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
        Spacer(Modifier.height(8.dp))
        Text(
            text = album.name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp
        )
        Text(
            text = album.genre,
            color = ColorTextHint,
            fontSize = 12.sp
        )
    }
}
