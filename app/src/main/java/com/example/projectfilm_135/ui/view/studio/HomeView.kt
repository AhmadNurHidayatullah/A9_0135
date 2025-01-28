package com.example.projectfilm_135.ui.view.studio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.model.Studio
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.HomeStudioUiState
import com.example.projectfilm_135.ui.viewmodel.studio.PenyediaStudioViewModel
import com.example.projectfilm_135.ui.viewmodel.studio.StudioViewModel

object DestinasiHomeStudio {
    const val route = "home_studio"
    const val titleRes = "Daftar Studio"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStudioScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: StudioViewModel = viewModel(factory = PenyediaStudioViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showDialog = remember { mutableStateOf(false) }
    val studioToDelete = remember { mutableStateOf<Studio?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = DestinasiHomeStudio.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getStudios()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Studio")
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Ensure padding to prevent overlap
        ) {
            StudioStatus(
                homeUiState = viewModel.studioUiState,
                retryAction = { viewModel.getStudios() },
                modifier = Modifier.fillMaxSize(),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    studioToDelete.value = it
                    showDialog.value = true
                }
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus studio ini?") },
                    confirmButton = {
                        Button(onClick = {
                            studioToDelete.value?.let { studio ->
                                viewModel.deleteStudio(studio.idStudio)
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
fun StudioStatus(
    homeUiState: HomeStudioUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Studio) -> Unit
) {
    when (homeUiState) {
        is HomeStudioUiState.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is HomeStudioUiState.Success ->
            if (homeUiState.studio.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data studio")
                }
            } else {
                StudioLayout(
                    studio = homeUiState.studio,
                    onDetailClick = { onDetailClick(it.idStudio.toString()) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        is HomeStudioUiState.Error -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Gagal memuat data studio")
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}

@Composable
fun StudioLayout(
    studio: List<Studio>,
    onDetailClick: (Studio) -> Unit,
    onDeleteClick: (Studio) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 100.dp
        ), // Extra padding for FAB
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(studio) { studio ->
            StudioCard(
                studio = studio,
                onDetailClick = { onDetailClick(studio) },
                onDeleteClick = { onDeleteClick(studio) }
            )
        }
    }
}

    @Composable
fun StudioCard(
    studio: Studio,
    onDetailClick: (Studio) -> Unit,
    onDeleteClick: (Studio) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onDetailClick(studio) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = " ${studio.namaStudio}", style = MaterialTheme.typography.titleLarge)


            // Delete button with margin
            IconButton(
                onClick = { onDeleteClick(studio) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}



