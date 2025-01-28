package com.example.projectfilm_135.ui.view.film

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel
import com.example.projectfilm_135.ui.viewmodel.film.UpdateFilmUiEvent
import com.example.projectfilm_135.ui.viewmodel.film.UpdateFilmUiState
import com.example.projectfilm_135.ui.viewmodel.film.UpdateFilmViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateFilm : DestinasiNavigasi {
    override val route = "update_film_view"
    override val titleRes = "Update Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFilmScreen(
    idFilm: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateFilmViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(idFilm) {
        viewModel.getFilmById(idFilm)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 8.dp),
        topBar = {
            TopAppBar(
                title = DestinasiUpdateFilm.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateFilmBody(
            updateFilmUiState = viewModel.updateFilmUiState,
            onFilmValueChange = viewModel::updateFilmState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateFilm()
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
fun UpdateFilmBody(
    updateFilmUiState: UpdateFilmUiState,
    onFilmValueChange: (UpdateFilmUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FilmFormInput(
            updateFilmUiEvent = updateFilmUiState.updateFilmUiEvent,
            onValueChange = onFilmValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Update",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmFormInput(
    updateFilmUiEvent: UpdateFilmUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateFilmUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = updateFilmUiEvent.judulFilm,
            onValueChange = { onValueChange(updateFilmUiEvent.copy(judulFilm = it)) },
            label = { Text("Judul Film") },
            placeholder = { Text("Masukkan Judul Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateFilmUiEvent.durasi.toString(),
            onValueChange = { onValueChange(updateFilmUiEvent.copy(durasi = it.toIntOrNull() ?: 0)) },
            label = { Text("Durasi (Menit)") },
            placeholder = { Text("Masukkan Durasi") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = updateFilmUiEvent.deskripsi,
            onValueChange = { onValueChange(updateFilmUiEvent.copy(deskripsi = it)) },
            label = { Text("Deskripsi") },
            placeholder = { Text("Masukkan Deskripsi Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        OutlinedTextField(
            value = updateFilmUiEvent.genre,
            onValueChange = { onValueChange(updateFilmUiEvent.copy(genre = it)) },
            label = { Text("Genre") },
            placeholder = { Text("Masukkan Genre Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateFilmUiEvent.ratingUsia,
            onValueChange = { onValueChange(updateFilmUiEvent.copy(ratingUsia = it)) },
            label = { Text("Rating Usia") },
            placeholder = { Text("Masukkan Rating Usia") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}
