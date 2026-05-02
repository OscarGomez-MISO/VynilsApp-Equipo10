package com.example.vynilsappequipo10.ui.collectors

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynilsappequipo10.domain.Collector
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

@Composable
fun CollectorsScreen(
    modifier: Modifier = Modifier,
    viewModel: CollectorsViewModel = viewModel()
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
                        "No se pudieron cargar los coleccionistas",
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
                        onClick = { viewModel.loadCollectors() },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }

        else -> {
            CollectorsContent(
                modifier = modifier,
                uiState = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange
            )
        }
    }
}

@Composable
private fun CollectorsContent(
    modifier: Modifier,
    uiState: CollectorsUiState,
    onSearchQueryChange: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBackground)
            .testTag("collectors_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "SOCIEDAD DE BUSCADORES DE JOYAS",
                    color = ColorOrangePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Los Coleccionistas",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Conoce a los curadores del resurgimiento analógico. Desde raras prensas de jazz hasta synth-pop experimental.",
                    color = ColorTextHint,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        item {
            CollectorsSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }

        if (uiState.collectors.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay coleccionistas registrados",
                        color = ColorTextHint,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            items(uiState.collectors) { collector ->
                CollectorRow(collector = collector)
            }
        }
    }
}

@Composable
private fun CollectorsSearchBar(
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
                    Text(
                        text = "Busca por nombre o experto en género...",
                        color = ColorTextHint,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    cursorBrush = SolidColor(ColorOrangePrimary),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("collector_search_field")
                )
            }
        }
    }
}

@Composable
private fun CollectorRow(collector: Collector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = collector.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = collector.email,
                color = ColorTextHint,
                fontSize = 12.sp
            )
        }

        if (collector.collectorAlbums.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(ColorOrangePrimary.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${collector.collectorAlbums.size} ${if (collector.collectorAlbums.size == 1) "ÁLBUM" else "ÁLBUMES"}",
                    color = ColorOrangePrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
