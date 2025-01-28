package com.example.projectfilm_135.ui.view.tiket


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
import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.ui.viewmodel.tiket.HomeTiketUiState
import com.example.projectfilm_135.ui.viewmodel.tiket.HomeTiketViewModel
import com.example.projectfilm_135.ui.viewmodel.tiket.PenyediaTiketViewModel

object DestinasiHomeTiket {
    val route = "home_tiket"
    val titleRes = "Daftar Tiket"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTiket(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeTiketViewModel = viewModel(factory = PenyediaTiketViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showDialog = remember { mutableStateOf(false) }
    val tiketToDelete = remember { mutableStateOf<Tiket?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            com.example.projectfilm_135.ui.customewidget.TopAppBar(
                title = DestinasiHomeTiket.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getTikets() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Tiket")
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TiketStatus(
                homeTiketUiState = viewModel.tiketUiState,
                retryAction = { viewModel.getTikets() },
                modifier = Modifier.fillMaxSize(),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    tiketToDelete.value = it
                    showDialog.value = true
                }
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah Anda yakin ingin menghapus Tiket ini?") },
                    confirmButton = {
                        Button(onClick = {
                            tiketToDelete.value?.let { tiket ->
                                viewModel.deleteTiket(tiket.idTiket)
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
fun TiketStatus(
    homeTiketUiState: HomeTiketUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Tiket) -> Unit
) {
    when (homeTiketUiState) {
        is HomeTiketUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("Loading...") }
        is HomeTiketUiState.Success ->
            TiketLayout(
                tikets = homeTiketUiState.tiketList,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        is HomeTiketUiState.Error -> Box(
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
fun TiketLayout(
    tikets: List<Tiket>,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Tiket) -> Unit
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
        items(tikets) { item ->
            TiketCard(
                tiket = item,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun TiketCard(
    tiket: Tiket,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Tiket) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDetailClick(tiket.idTiket.toString()) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID Tiket: ${tiket.idTiket}")
            Text(text = "ID Penayangan: ${tiket.idPenayangan}")
            Text(text = "Jumlah Tiket: ${tiket.jumlahTiket}")
            Text(text = "Total Harga: ${tiket.totalHarga}")
            Text(text = "Status Pembayaran: ${tiket.statusPembayaran}")
            IconButton(onClick = { onDeleteClick(tiket) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus")
            }
        }
    }
}
