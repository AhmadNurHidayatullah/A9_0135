package com.example.projectfilm_135.ui.view.studio


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
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.PenyediaStudioViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.UpdateStudioUiEvent
import com.example.projectfilm_135.ui.viewmodel.studio.UpdateStudioUiState
import com.example.projectfilm_135.ui.viewmodel.studio.UpdateStudioViewModel
import kotlinx.coroutines.launch

object DestinasiUpdateStudio : DestinasiNavigasi {
    override val route = "update_studio_view"
    override val titleRes = "Update Studio"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStudioScreen(
    idStudio: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateStudioViewModel = viewModel(factory = PenyediaStudioViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(idStudio) {
        viewModel.getStudioById(idStudio)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 8.dp),
        topBar = {
            TopAppBar(
                title = DestinasiUpdateStudio.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateStudioBody(
            updateStudioUiState = viewModel.updateStudioUiState,
            onStudioValueChange = viewModel::updateStudioState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateStudio()
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
fun UpdateStudioBody(
    updateStudioUiState: UpdateStudioUiState,
    onStudioValueChange: (UpdateStudioUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        StudioFormInput(
            updateStudioUiEvent = updateStudioUiState.updateStudioUiEvent,
            onValueChange = onStudioValueChange,
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
fun StudioFormInput(
    updateStudioUiEvent: UpdateStudioUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateStudioUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = updateStudioUiEvent.namaStudio,
            onValueChange = { onValueChange(updateStudioUiEvent.copy(namaStudio = it)) },
            label = { Text("Nama Studio") },
            placeholder = { Text("Masukkan Nama Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateStudioUiEvent.kapasitas.toString(),
            onValueChange = { onValueChange(updateStudioUiEvent.copy(kapasitas = it.toIntOrNull() ?: 0)) },
            label = { Text("Kapasitas") },
            placeholder = { Text("Masukkan Kapasitas Studio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = true,
            singleLine = true
        )
    }
}
