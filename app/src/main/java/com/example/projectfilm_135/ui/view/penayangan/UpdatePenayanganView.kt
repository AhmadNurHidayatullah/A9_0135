package com.example.projectfilm_135.ui.view.penayangan


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.projectfilm_135.ui.viewmodel.penayangan.UpdatePenayanganUiEvent
import com.example.projectfilm_135.ui.viewmodel.penayangan.UpdatePenayanganUiState
import com.example.projectfilm_135.ui.viewmodel.penayangan.UpdatePenayanganViewModel
import com.example.projectfilm_135.ui.viewmodel.penayangan.PenyediaPenayanganViewModel
import kotlinx.coroutines.launch

object DestinasiUpdatePenayangan : DestinasiNavigasi {
    override val route = "update_penayangan_view"
    override val titleRes = "Update Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePenayanganScreen(
    idPenayangan: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePenayanganViewModel = viewModel(factory = PenyediaPenayanganViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Memuat data penayangan berdasarkan ID
    LaunchedEffect(idPenayangan) {
        viewModel.loadPenayanganById(idPenayangan)
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(horizontal = 8.dp),
        topBar = {
            TopAppBar(
                title = DestinasiUpdatePenayangan.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdatePenayanganBody(
            updatePenayanganUiState = viewModel.uiState,
            onPenayanganValueChange = viewModel::updatePenayanganState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveUpdatedPenayangan()
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
fun UpdatePenayanganBody(
    updatePenayanganUiState: UpdatePenayanganUiState,
    onPenayanganValueChange: (UpdatePenayanganUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        PenayanganFormInput(
            updatePenayanganUiEvent = updatePenayanganUiState.updatePenayanganUiEvent,
            onValueChange = onPenayanganValueChange,
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
fun PenayanganFormInput(
    updatePenayanganUiEvent: UpdatePenayanganUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdatePenayanganUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = updatePenayanganUiEvent.idFilm.toString(),
            onValueChange = { onValueChange(updatePenayanganUiEvent.copy(idFilm = it.toIntOrNull() ?: 0)) },
            label = { Text("ID Film") },
            placeholder = { Text("Masukkan ID Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = updatePenayanganUiEvent.idStudio.toString(),
            onValueChange = { onValueChange(updatePenayanganUiEvent.copy(idStudio = it.toIntOrNull() ?: 0)) },
            label = { Text("ID Studio") },
            placeholder = { Text("Masukkan ID Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = updatePenayanganUiEvent.tanggalPenayangan ?: "",
            onValueChange = { inputDate ->
                onValueChange(
                    updatePenayanganUiEvent.copy(tanggalPenayangan = inputDate)
                )
            },
            label = { Text("Tanggal dan Waktu Penayangan") },
            placeholder = { Text("YYYY-MM-DD HH:mm") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        OutlinedTextField(
            value = updatePenayanganUiEvent.hargaTiket.toString(),
            onValueChange = { onValueChange(updatePenayanganUiEvent.copy(hargaTiket = it.toDoubleOrNull() ?: 0.0)) },
            label = { Text("Harga Tiket") },
            placeholder = { Text("Masukkan Harga Tiket") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = true,
            singleLine = true
        )
    }
}
