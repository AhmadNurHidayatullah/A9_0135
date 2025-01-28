package com.example.projectfilm_135.ui.view.film

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.film.InsertFilmUiEvent
import com.example.projectfilm_135.ui.viewmodel.film.InsertFilmUiState
import com.example.projectfilm_135.ui.viewmodel.film.InsertFilmViewModel
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel
import kotlinx.coroutines.launch

object FilmEntry : DestinasiNavigasi {
    override val route = "film_entry"
    override val titleRes = "Entry Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryFilmScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertFilmViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = FilmEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        FilmEntryBody(
            insertFilmUiState = viewModel.uiState,
            onFilmValueChange = viewModel::updateInsertFilmState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertFilm()
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
fun FilmEntryBody(
    insertFilmUiState: InsertFilmUiState,
    onFilmValueChange: (InsertFilmUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(8.dp)
    ) {
        FilmFormInput(
            insertFilmUiEvent = insertFilmUiState.insertFilmUiEvent,
            onValueChange = onFilmValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmFormInput(
    insertFilmUiEvent: InsertFilmUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertFilmUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = insertFilmUiEvent.idFilm.toString(),
            onValueChange = {
                onValueChange(insertFilmUiEvent.copy(idFilm = it.toIntOrNull() ?: 0))
            },
            label = { Text("ID Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )


        OutlinedTextField(
            value = insertFilmUiEvent.judulFilm,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(judulFilm = it)) },
            label = { Text("Judul Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertFilmUiEvent.durasi.toString(),
            onValueChange = {
                onValueChange(insertFilmUiEvent.copy(durasi = it.toIntOrNull() ?: 0))
            },
            label = { Text("Durasi (menit)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = insertFilmUiEvent.deskripsi,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(deskripsi = it)) },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        OutlinedTextField(
            value = insertFilmUiEvent.genre,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(genre = it)) },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertFilmUiEvent.ratingUsia,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(ratingUsia = it)) },
            label = { Text("Rating Usia") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data Film!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
