package com.example.vynilsappequipo10.ui.albums.albumDetail

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.Performer
import com.example.vynilsappequipo10.domain.Track
import com.example.vynilsappequipo10.ui.main.UserSession
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumId: Int,
    isCollector: Boolean = false,
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCommentSheet by remember { mutableStateOf(false) }

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
        floatingActionButton = {
            if (isCollector) {
                ExtendedFloatingActionButton(
                    onClick = { showCommentSheet = true },
                    containerColor = ColorOrangePrimary,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Agregar Comentario") },
                    modifier = Modifier.testTag("add_comment_fab")
                )
            }
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

        if (showCommentSheet) {
            CommentSheet(
                albumId = albumId,
                onDismiss = { 
                    showCommentSheet = false
                    viewModel.loadAlbum(albumId)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(albumId: Int, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val userSession = remember { UserSession(context.applicationContext) }
    val commentViewModel: CommentViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return CommentViewModel(userSession = userSession) as T
        }
    })
    
    val state by commentViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    
    var email by rememberSaveable { mutableStateOf(userSession.getCollectorEmail() ?: "") }
    var rating by rememberSaveable { mutableIntStateOf(5) }
    var description by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var telephone by rememberSaveable { mutableStateOf("") }
    var isChangingUser by rememberSaveable(userSession.getCollectorId()) { 
        mutableStateOf(userSession.getCollectorId() == -1) 
    }

    // Sync state and email when the sheet enters composition
    LaunchedEffect(Unit) {
        commentViewModel.resetState()
        val currentId = userSession.getCollectorId()
        val currentEmail = userSession.getCollectorEmail() ?: ""
        
        if (currentId != -1) {
            email = currentEmail
            isChangingUser = false
        }
        Log.d("CommentSheet", "Opening sheet. Session ID: $currentId, Email: $currentEmail")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ColorSurface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Agregar Comentario", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            when (val currentState = state) {
                is CommentUiState.Loading -> CircularProgressIndicator(color = ColorOrangePrimary, modifier = Modifier.align(Alignment.CenterHorizontally))
                is CommentUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                            .testTag("comment_success_message"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, // Usando Star como placeholder de éxito
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = currentState.message,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LaunchedEffect(currentState) { 
                        delay(1500) // Mostrar el mensaje por 1.5 segundos
                        onDismiss() 
                    }
                }
                is CommentUiState.Error -> {
                    Column {
                        Text(currentState.message, color = Color.Red)
                        Button(onClick = { commentViewModel.resetState() }) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    if (!isChangingUser && userSession.getCollectorId() != -1) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Publicando como: ", color = ColorTextHint, fontSize = 14.sp)
                                Text(
                                    text = userSession.getCollectorEmail() ?: "",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            TextButton(
                                onClick = { isChangingUser = true },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("¿No eres tú? Cambiar cuenta", color = ColorOrangePrimary, fontSize = 12.sp)
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") },
                            modifier = Modifier.fillMaxWidth().testTag("comment_email_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ColorOrangePrimary,
                                unfocusedBorderColor = ColorTextHint
                            )
                        )
                    }

                    if (currentState is CommentUiState.NeedProfile) {
                        Text("Perfil no encontrado. Por favor completa tus datos:", color = ColorOrangePrimary, fontSize = 14.sp)
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre Completo") },
                            modifier = Modifier.fillMaxWidth().testTag("comment_name_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ColorOrangePrimary,
                                unfocusedBorderColor = ColorTextHint
                            )
                        )
                        OutlinedTextField(
                            value = telephone,
                            onValueChange = { telephone = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth().testTag("comment_phone_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = ColorOrangePrimary,
                                unfocusedBorderColor = ColorTextHint
                            )
                        )
                    }

                    Text("Calificación", color = Color.White)
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        repeat(5) { index ->
                            IconButton(onClick = { rating = index + 1 }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < rating) ColorOrangePrimary else Color.Gray.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Tu comentario") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("comment_description_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = ColorOrangePrimary,
                            unfocusedBorderColor = ColorTextHint
                        )
                    )

                    Button(
                        onClick = { 
                            commentViewModel.postComment(
                                albumId, description, rating, email, 
                                if (name.isNotEmpty()) name else null, 
                                if (telephone.isNotEmpty()) telephone else null
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp).testTag("comment_send_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary),
                        enabled = description.isNotEmpty() && email.isNotEmpty()
                    ) {
                        Text("Enviar Comentario")
                    }
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
        contentPadding = PaddingValues(bottom = 88.dp)
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
    val context = LocalContext.current
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
            }
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
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
            },
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
    val context = LocalContext.current
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Toast.makeText(context, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
            }
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
