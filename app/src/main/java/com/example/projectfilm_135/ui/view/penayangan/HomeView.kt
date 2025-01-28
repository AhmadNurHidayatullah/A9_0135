package com.example.projectfilm_135.ui.view.penayangan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.ui.view.studio.StudioCard
import com.example.projectfilm_135.ui.viewmodel.penayangan.HomePenayanganUiState
import com.example.projectfilm_135.ui.viewmodel.penayangan.HomePenayanganViewModel
import com.example.projectfilm_135.ui.viewmodel.penayangan.PenyediaPenayanganViewModel


object DestinasiHomePenayangan {
    val route = "home_penayangan"
    val titleRes = "Daftar Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPenayangan(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomePenayanganViewModel = viewModel(factory = PenyediaPenayanganViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showDialog = remember { mutableStateOf(false) }
    val penayanganToDelete = remember { mutableStateOf<Penayangan?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            com.example.projectfilm_135.ui.customewidget.TopAppBar(
                title = DestinasiHomePenayangan.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getPenayangan() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Penayangan")
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PenayanganStatus(
                homePenayanganUiState = viewModel.penayanganUiState,
                retryAction = { viewModel.getPenayangan() },
                modifier = Modifier.fillMaxSize(),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    penayanganToDelete.value = it
                    showDialog.value = true
                }
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus Penayangan ini?") },
                    confirmButton = {
                        Button(onClick = {
                            penayanganToDelete.value?.let { penayangan ->
                                viewModel.deletePenayangan(penayangan.idPenayangan)
                            }
                            showDialog.value = false
                        }) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog.value = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PenayanganStatus(
    homePenayanganUiState: HomePenayanganUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Penayangan) -> Unit
) {
    when (homePenayanganUiState) {
        is HomePenayanganUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("Loading...") }
        is HomePenayanganUiState.Success ->
            PenayanganLayout(
                penayangan = homePenayanganUiState.penayanganList,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        is HomePenayanganUiState.Error -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Gagal Memuat Data")
                Button(onClick = retryAction) { Text("Coba Lagi") }
            }
        }
    }
}

@Composable
fun PenayanganLayout(
    penayangan: List<Penayangan>,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Penayangan) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(penayangan) { item ->
            PenayanganCard(
                penayangan = item,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun PenayanganCard(
    penayangan: Penayangan,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Penayangan) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDetailClick(penayangan.idPenayangan.toString()) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID Penayangan: ${penayangan.idPenayangan}")
            Text(text = "ID Film: ${penayangan.idFilm}")
            Text(text = "ID Studio: ${penayangan.idStudio}")
            IconButton(onClick = { onDeleteClick(penayangan) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus")
            }
        }
    }
}


