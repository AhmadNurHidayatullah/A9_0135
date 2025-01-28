package com.example.projectfilm_135.ui.view.tiket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.model.StatusPembayaran
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.tiket.InsertTiketViewModel
import com.example.projectfilm_135.ui.viewmodel.tiket.PenyediaTiketViewModel
import kotlinx.coroutines.launch

object TiketEntry : DestinasiNavigasi {
    override val route = "tiket_entry"
    override val titleRes = "Entry Tiket"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTiketView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: InsertTiketViewModel = viewModel(factory = PenyediaTiketViewModel.Factory)
) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pembelian Tiket") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error message if any
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Selected Penayangan Info
            uiState.insertTiketUiEvent.hargaTiket?.let { harga ->
                Text(
                    text = "Harga Tiket: Rp $harga",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Input ID Penayangan
            OutlinedTextField(
                value = uiState.insertTiketUiEvent.idPenayangan?.toString() ?: "",
                onValueChange = {
                    viewModel.clearError()
                    viewModel.updateIdPenayangan(it.toIntOrNull() ?: 0)
                },
                label = { Text("ID Penayangan") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Input ID Tiket
            OutlinedTextField(
                value = uiState.insertTiketUiEvent.idTiket?.toString() ?: "",
                onValueChange = {
                    viewModel.clearError()
                    viewModel.updateIdTiket(it.toIntOrNull() ?: 0)
                },
                label = { Text("ID Tiket") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Input Jumlah Tiket
            OutlinedTextField(
                value = uiState.insertTiketUiEvent.jumlahTiket?.toString() ?: "",
                onValueChange = {
                    viewModel.clearError()
                    val number = it.toIntOrNull() ?: 0
                    viewModel.updateJumlahTiket(number)
                },
                label = { Text("Jumlah Tiket") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Display Total Harga
            Text(
                text = "Total Harga: Rp ${uiState.insertTiketUiEvent.totalHarga?.toString() ?: "0"}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Status Pembayaran Selection
            Column {
                Text(
                    text = "Status Pembayaran",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                StatusPembayaran.values().forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = uiState.insertTiketUiEvent.statusPembayaran == status,
                                onClick = { viewModel.updateStatusPembayaran(status) }
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.insertTiketUiEvent.statusPembayaran == status,
                            onClick = { viewModel.updateStatusPembayaran(status) }
                        )
                        Text(
                            text = when (status) {
                                StatusPembayaran.LUNAS -> "Lunas"
                                StatusPembayaran.BELUM_LUNAS -> "Belum Lunas"
                            },

                        )
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    scope.launch {
                        try {
                            viewModel.insertTiket()
                            navigateBack()
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Simpan Tiket")
            }
        }
    }
}
