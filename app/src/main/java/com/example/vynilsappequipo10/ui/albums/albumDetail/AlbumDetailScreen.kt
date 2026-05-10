package com.example.vynilsappequipo10.ui.albums.albumDetail

import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vynilsappequipo10.R
import com.example.vynilsappequipo10.domain.Album
import com.example.vynilsappequipo10.domain.Comment
import com.example.vynilsappequipo10.domain.Performer
import com.example.vynilsappequipo10.domain.Track
import com.example.vynilsappequipo10.ui.main.UserSession
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint
import kotlinx.coroutines.delay

private const val COMMENT_SUCCESS_DELAY_MS = 1500L
private const val MAX_RATING = 5

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
                title = { Text(stringResource(R.string.album_detail_title), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.create_album_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)
            )
        },
        floatingActionButton = {
            if (isCollector) {
                ExtendedFloatingActionButton(
                    onClick = { showCommentSheet = true },
                    containerColor = ColorOrangePrimary,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text(stringResource(R.string.album_detail_add_comment)) },
                    modifier = Modifier.testTag("add_comment_fab")
                )
            }
        },
        containerColor = ColorBackground
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            val album = uiState.album
            val error = uiState.error
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ColorOrangePrimary
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.album_detail_error_prefix, error), color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadAlbum(albumId) },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary)
                        ) {
                            Text(stringResource(R.string.album_detail_retry))
                        }
                    }
                }
                album != null -> AlbumDetailContent(album)
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
    val commentViewModel: CommentViewModel = viewModel { CommentViewModel(userSession = userSession) }

    val state by commentViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val collectorId = userSession.getCollectorId()

    var email by rememberSaveable { mutableStateOf(userSession.getCollectorEmail() ?: "") }
    var rating by rememberSaveable { mutableIntStateOf(MAX_RATING) }
    var description by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var telephone by rememberSaveable { mutableStateOf("") }
    var isChangingUser by rememberSaveable(collectorId) {
        mutableStateOf(collectorId == -1)
    }

    LaunchedEffect(Unit) {
        commentViewModel.resetState()
        if (collectorId != -1) {
            email = userSession.getCollectorEmail() ?: ""
            isChangingUser = false
        }
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
                .padding(bottom = 32.dp)
                .testTag("comment_sheet_content"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(R.string.comment_sheet_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            when (val currentState = state) {
                is CommentUiState.Loading -> CircularProgressIndicator(
                    color = ColorOrangePrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                is CommentUiState.Success -> CommentSheetSuccess(currentState, onDismiss)
                is CommentUiState.Error -> CommentSheetError(currentState) { commentViewModel.resetState() }
                else -> CommentSheetForm(
                    state = currentState,
                    userSession = userSession,
                    collectorId = collectorId,
                    email = email,
                    onEmailChange = { email = it },
                    rating = rating,
                    onRatingChange = { rating = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    name = name,
                    onNameChange = { name = it },
                    telephone = telephone,
                    onTelephoneChange = { telephone = it },
                    isChangingUser = isChangingUser,
                    onChangeUser = { isChangingUser = true },
                    onSend = {
                        commentViewModel.postComment(
                            albumId, description, rating, email,
                            name.ifEmpty { null },
                            telephone.ifEmpty { null }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CommentSheetSuccess(state: CommentUiState.Success, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .testTag("comment_success_message"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(text = state.message, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
    LaunchedEffect(state) {
        delay(COMMENT_SUCCESS_DELAY_MS)
        onDismiss()
    }
}

@Composable
private fun CommentSheetError(state: CommentUiState.Error, onRetry: () -> Unit) {
    Column(modifier = Modifier.testTag("comment_error_message")) {
        Text(state.message, color = Color.Red)
        Button(onClick = onRetry) {
            Text(stringResource(R.string.album_detail_retry))
        }
    }
}

@Composable
private fun CommentSheetForm(
    state: CommentUiState,
    userSession: UserSession,
    collectorId: Int,
    email: String,
    onEmailChange: (String) -> Unit,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    telephone: String,
    onTelephoneChange: (String) -> Unit,
    isChangingUser: Boolean,
    onChangeUser: () -> Unit,
    onSend: () -> Unit
) {
    if (!isChangingUser && collectorId != -1) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.comment_publishing_as), color = ColorTextHint, fontSize = 14.sp)
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
                onClick = onChangeUser,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(stringResource(R.string.comment_change_account), color = ColorOrangePrimary, fontSize = 12.sp)
            }
        }
    } else {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.comment_field_email)) },
            modifier = Modifier.fillMaxWidth().testTag("comment_email_field"),
            colors = commentFieldColors()
        )
    }

    if (state is CommentUiState.NeedProfile) {
        Text(stringResource(R.string.comment_profile_not_found), color = ColorOrangePrimary, fontSize = 14.sp)
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.comment_field_name)) },
            modifier = Modifier.fillMaxWidth().testTag("comment_name_field"),
            colors = commentFieldColors()
        )
        OutlinedTextField(
            value = telephone,
            onValueChange = onTelephoneChange,
            label = { Text(stringResource(R.string.comment_field_phone)) },
            modifier = Modifier.fillMaxWidth().testTag("comment_phone_field"),
            colors = commentFieldColors()
        )
    }

    Text(stringResource(R.string.comment_field_rating), color = Color.White)
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        repeat(MAX_RATING) { index ->
            IconButton(onClick = { onRatingChange(index + 1) }) {
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
        onValueChange = onDescriptionChange,
        label = { Text(stringResource(R.string.comment_field_description)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .testTag("comment_description_field"),
        colors = commentFieldColors()
    )

    Button(
        onClick = onSend,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("comment_send_button"),
        colors = ButtonDefaults.buttonColors(containerColor = ColorOrangePrimary),
        enabled = description.isNotEmpty() && email.isNotEmpty()
    ) {
        Text(stringResource(R.string.comment_btn_send))
    }
}

@Composable
private fun commentFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = ColorOrangePrimary,
    unfocusedBorderColor = ColorTextHint
)

@Composable
private fun AlbumDetailContent(album: Album) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        albumCoverSection(album)
        albumDescriptionSection(album)
        albumTracksSection(album.tracks)
        albumPerformersSection(album.performers)
        albumCommentsSection(album.comments)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
private fun LazyListScope.albumCoverSection(album: Album) {
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
                Text(album.name, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "${album.genre} • ${album.recordLabel}",
                    color = ColorOrangePrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.album_release_date, album.releaseDate.take(10)),
                    color = ColorTextHint,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun LazyListScope.albumDescriptionSection(album: Album) {
    item {
        SectionTitle(stringResource(R.string.album_detail_description))
        Text(text = album.description, color = Color.White, fontSize = 14.sp, lineHeight = 22.sp)
    }
}

private fun LazyListScope.albumTracksSection(tracks: List<Track>) {
    if (tracks.isEmpty()) return
    item { SectionTitle(stringResource(R.string.album_detail_tracks)) }
    items(tracks, key = { "track_${it.id}" }) { TrackItem(it) }
}

private fun LazyListScope.albumPerformersSection(performers: List<Performer>) {
    if (performers.isEmpty()) return
    item { SectionTitle(stringResource(R.string.album_detail_performers)) }
    items(performers, key = { "performer_${it.id}" }) { PerformerItem(it) }
}

private fun LazyListScope.albumCommentsSection(comments: List<Comment>) {
    if (comments.isEmpty()) return
    item { SectionTitle(stringResource(R.string.album_detail_comments)) }
    items(comments, key = { "comment_${it.id}" }) { CommentItem(it) }
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
    val msgDevelopment = stringResource(R.string.msg_feature_in_development)
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Toast.makeText(context, msgDevelopment, Toast.LENGTH_SHORT).show() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = track.name, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text(text = track.duration, color = ColorTextHint, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PerformerItem(performer: Performer) {
    val context = LocalContext.current
    val msgDevelopment = stringResource(R.string.msg_feature_in_development)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Toast.makeText(context, msgDevelopment, Toast.LENGTH_SHORT).show() },
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
            Text(performer.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
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
    val msgDevelopment = stringResource(R.string.msg_feature_in_development)
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Toast.makeText(context, msgDevelopment, Toast.LENGTH_SHORT).show() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(MAX_RATING) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < comment.rating) ColorOrangePrimary else Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(text = comment.description, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}
