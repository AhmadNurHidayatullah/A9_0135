package com.example.projectfilm_135.ui.view.studio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.InsertStudioUiEvent
import com.example.projectfilm_135.ui.viewmodel.studio.InsertStudioUiState
import com.example.projectfilm_135.ui.viewmodel.studio.InsertStudioViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.PenyediaStudioViewModel
import kotlinx.coroutines.launch

object StudioEntry : DestinasiNavigasi {
    override val route = "studio_entry"
    override val titleRes = "Entry Studio"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryStudioScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertStudioViewModel = viewModel(factory = PenyediaStudioViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = StudioEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        StudioEntryBody(
            insertStudioUiState = viewModel.uiState,
            onStudioValueChange = viewModel::updateInsertStudioState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertStudio()
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
fun StudioEntryBody(
    insertStudioUiState: InsertStudioUiState,
    onStudioValueChange: (InsertStudioUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(8.dp)
    ) {
        StudioFormInput(
            insertStudioUiEvent = insertStudioUiState.insertStudioUiEvent,
            onValueChange = onStudioValueChange,
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
fun StudioFormInput(
    insertStudioUiEvent: InsertStudioUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertStudioUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = insertStudioUiEvent.idStudio.toString(),
            onValueChange = {
                onValueChange(insertStudioUiEvent.copy(idStudio = it.toIntOrNull() ?: 0))
            },
            label = { Text("ID Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = insertStudioUiEvent.namaStudio,
            onValueChange = { onValueChange(insertStudioUiEvent.copy(namaStudio = it)) },
            label = { Text("Nama Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = insertStudioUiEvent.kapasitas.toString(),
            onValueChange = {
                onValueChange(insertStudioUiEvent.copy(kapasitas = it.toIntOrNull() ?: 0))
            },
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        if (enabled) {
            Text(
                text = "Isi Semua Data Studio!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}