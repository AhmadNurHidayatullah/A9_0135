package com.example.projectfilm_135.ui.view.penayangan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.penayangan.*
import kotlinx.coroutines.launch
import com.example.projectfilm_135.ui.viewmodel.penayangan.InsertPenayanganUiEvent

object PenayanganEntry : DestinasiNavigasi {
    override val route = "penayangan_entry"
    override val titleRes = "Entry Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPenayanganScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPenayanganViewModel = viewModel(factory = PenyediaPenayanganViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = PenayanganEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        PenayanganEntryBody(
            insertPenayanganUiState = viewModel.uiState,
            onPenayanganValueChange = viewModel::updateInsertPenayanganState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPenayangan()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun PenayanganEntryBody(
    insertPenayanganUiState: InsertPenayanganUiState,
    onPenayanganValueChange: (InsertPenayanganUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(8.dp)
    ) {
        PenayanganFormInput(
            insertPenayanganUiEvent = insertPenayanganUiState.insertPenayanganUiEvent,
            filmList = insertPenayanganUiState.filmList,
            studioList = insertPenayanganUiState.studioList,
            onValueChange = onPenayanganValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan Penayangan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenayanganFormInput(
    insertPenayanganUiEvent: InsertPenayanganUiEvent,
    filmList: List<Film>,
    studioList: List<Studio>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertPenayanganUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    var expandedFilm by remember { mutableStateOf(false) }
    var expandedStudio by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ID Penayangan TextField
        OutlinedTextField(
            value = insertPenayanganUiEvent.idPenayangan?.toString() ?: "",
            onValueChange = {
                onValueChange(
                    insertPenayanganUiEvent.copy(
                        idPenayangan = it.toIntOrNull() ?: 0
                    )
                )
            },
            label = { Text("ID Penayangan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Film Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedFilm,
            onExpandedChange = { expandedFilm = !expandedFilm },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = insertPenayanganUiEvent.namaFilm ?: "Pilih Film",
                onValueChange = {},
                readOnly = true,
                label = { Text("Film") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilm)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedFilm,
                onDismissRequest = { expandedFilm = false }
            ) {
                filmList.forEach { film ->
                    DropdownMenuItem(
                        text = { Text(film.judulFilm) },
                        onClick = {
                            onValueChange(
                                insertPenayanganUiEvent.copy(
                                    idFilm = film.idFilm,
                                    namaFilm = film.judulFilm
                                )
                            )
                            expandedFilm = false
                        }
                    )
                }
            }
        }

        // Studio Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedStudio,
            onExpandedChange = { expandedStudio = !expandedStudio },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = insertPenayanganUiEvent.namaStudio ?: "Pilih Studio",
                onValueChange = {},
                readOnly = true,
                label = { Text("Studio") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStudio)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedStudio,
                onDismissRequest = { expandedStudio = false }
            ) {
                studioList.forEach { studio ->
                    DropdownMenuItem(
                        text = { Text(studio.namaStudio) },
                        onClick = {
                            onValueChange(
                                insertPenayanganUiEvent.copy(
                                    idStudio = studio.idStudio,
                                    namaStudio = studio.namaStudio
                                )
                            )
                            expandedStudio = false
                        }
                    )
                }
            }
        }

        // Tanggal Penayangan
        OutlinedTextField(
            value = insertPenayanganUiEvent.tanggalPenayangan ?: "",
            onValueChange = { inputDate ->
                onValueChange(
                    insertPenayanganUiEvent.copy(tanggalPenayangan = inputDate)
                )
            },
            label = { Text("Tanggal dan Waktu Penayangan") },
            placeholder = { Text("YYYY-MM-DD HH:mm") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Harga Tiket
        OutlinedTextField(
            value = insertPenayanganUiEvent.hargaTiket?.toString() ?: "",
            onValueChange = { inputPrice ->
                val price = inputPrice.toDoubleOrNull() ?: 0.0
                onValueChange(
                    insertPenayanganUiEvent.copy(hargaTiket = price)
                )
            },
            label = { Text("Harga Tiket") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        if (enabled) {
            Text(
                text = "Lengkapi Semua Data Penayangan!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}