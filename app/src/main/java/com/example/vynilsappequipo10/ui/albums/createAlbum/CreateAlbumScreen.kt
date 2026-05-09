package com.example.vynilsappequipo10.ui.albums.createAlbum

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import com.example.vynilsappequipo10.R
import com.example.vynilsappequipo10.ui.theme.ColorBackground
import com.example.vynilsappequipo10.ui.theme.ColorOrangePrimary
import com.example.vynilsappequipo10.ui.theme.ColorSurface
import com.example.vynilsappequipo10.ui.theme.ColorTextHint
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(
    onBackClick: () -> Unit,
    onAlbumCreated: () -> Unit,
    viewModel: CreateAlbumViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_album_title), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.create_album_back),
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is CreateAlbumUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ColorOrangePrimary
                    )
                }
                is CreateAlbumUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                            .testTag("create_album_success"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.create_album_success),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.album.name,
                            color = ColorOrangePrimary,
                            fontSize = 16.sp
                        )
                    }
                    LaunchedEffect(state) {
                        delay(1500)
                        viewModel.resetForm()
                        onAlbumCreated()
                    }
                }
                is CreateAlbumUiState.Error -> {
                    CreateAlbumForm(
                        formState = formState,
                        viewModel = viewModel,
                        errorMessage = state.message,
                        isFormValid = viewModel.isFormValid.collectAsState().value
                    )
                }
                else -> {
                    CreateAlbumForm(
                        formState = formState,
                        viewModel = viewModel,
                        errorMessage = null,
                        isFormValid = viewModel.isFormValid.collectAsState().value
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateAlbumForm(
    formState: AlbumFormState,
    viewModel: CreateAlbumViewModel,
    errorMessage: String?,
    isFormValid: Boolean = false
) {
    val context = LocalContext.current
    var expandedGenre by remember { mutableStateOf(false) }
    var expandedRecordLabel by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.create_album_info_title),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        if (errorMessage != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
        }

        // Campo Nombre
        OutlinedTextField(
            value = formState.name,
            onValueChange = viewModel::updateName,
            label = { Text(stringResource(R.string.create_album_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("album_name_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ColorOrangePrimary,
                unfocusedBorderColor = ColorTextHint,
                focusedLabelColor = ColorOrangePrimary,
                unfocusedLabelColor = ColorTextHint
            ),
            singleLine = true
        )

        // Campo URL de Portada
        OutlinedTextField(
            value = formState.cover,
            onValueChange = viewModel::updateCover,
            label = { Text(stringResource(R.string.create_album_cover)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("album_cover_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ColorOrangePrimary,
                unfocusedBorderColor = ColorTextHint,
                focusedLabelColor = ColorOrangePrimary,
                unfocusedLabelColor = ColorTextHint
            ),
            singleLine = true
        )

        // Campo Fecha de Lanzamiento
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                viewModel.updateReleaseDate(dateFormat.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        OutlinedTextField(
            value = if (formState.releaseDate.isNotBlank()) {
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    val date = inputFormat.parse(formState.releaseDate)
                    outputFormat.format(date!!)
                } catch (e: Exception) {
                    formState.releaseDate
                }
            } else "",
            onValueChange = { },
            label = { Text(stringResource(R.string.create_album_date)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
                .testTag("album_date_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ColorOrangePrimary,
                unfocusedBorderColor = ColorTextHint,
                focusedLabelColor = ColorOrangePrimary,
                unfocusedLabelColor = ColorTextHint,
                disabledTextColor = Color.White,
                disabledBorderColor = ColorTextHint,
                disabledLabelColor = ColorTextHint
            ),
            enabled = false,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.create_album_select_date),
                        tint = ColorOrangePrimary
                    )
                }
            }
        )

        // Campo Descripción
        OutlinedTextField(
            value = formState.description,
            onValueChange = viewModel::updateDescription,
            label = { Text(stringResource(R.string.album_detail_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .testTag("album_description_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ColorOrangePrimary,
                unfocusedBorderColor = ColorTextHint,
                focusedLabelColor = ColorOrangePrimary,
                unfocusedLabelColor = ColorTextHint
            ),
            maxLines = 4
        )

        // Dropdown Género
        ExposedDropdownMenuBox(
            expanded = expandedGenre,
            onExpandedChange = { expandedGenre = !expandedGenre }
        ) {
            OutlinedTextField(
                value = formState.genre,
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.create_album_genre)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("album_genre_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = ColorOrangePrimary,
                    unfocusedBorderColor = ColorTextHint,
                    focusedLabelColor = ColorOrangePrimary,
                    unfocusedLabelColor = ColorTextHint
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = ColorOrangePrimary
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expandedGenre,
                onDismissRequest = { expandedGenre = false },
                modifier = Modifier.background(ColorSurface)
            ) {
                viewModel.genres.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre, color = Color.White) },
                        onClick = {
                            viewModel.updateGenre(genre)
                            expandedGenre = false
                        },
                        modifier = Modifier.testTag("genre_option_$genre")
                    )
                }
            }
        }

        // Dropdown Sello Discográfico
        ExposedDropdownMenuBox(
            expanded = expandedRecordLabel,
            onExpandedChange = { expandedRecordLabel = !expandedRecordLabel }
        ) {
            OutlinedTextField(
                value = formState.recordLabel,
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.create_album_record_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("album_record_label_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = ColorOrangePrimary,
                    unfocusedBorderColor = ColorTextHint,
                    focusedLabelColor = ColorOrangePrimary,
                    unfocusedLabelColor = ColorTextHint
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = ColorOrangePrimary
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expandedRecordLabel,
                onDismissRequest = { expandedRecordLabel = false },
                modifier = Modifier.background(ColorSurface)
            ) {
                viewModel.recordLabels.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label, color = Color.White) },
                        onClick = {
                            viewModel.updateRecordLabel(label)
                            expandedRecordLabel = false
                        },
                        modifier = Modifier.testTag("record_label_option_$label")
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón Crear
        Button(
            onClick = { viewModel.createAlbum() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("create_album_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorOrangePrimary,
                disabledContainerColor = ColorOrangePrimary.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.create_album_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}
