package com.example.projectfilm_135.ui.view.penayangan

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.penayangan.DetailPenayanganUiState
import com.example.projectfilm_135.ui.viewmodel.penayangan.DetailPenayanganViewModel
import com.example.projectfilm_135.ui.viewmodel.penayangan.PenyediaPenayanganViewModel

object DestinasiDetailPenayangan : DestinasiNavigasi {
    override val route = "detail_penayangan"
    override val titleRes = "Detail Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPenayanganView(
    idPenayangan: Int,
    navigateBack: () -> Unit,
    onClick: () -> Unit,
    viewModel: DetailPenayanganViewModel = viewModel(factory = PenyediaPenayanganViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Fetch data saat idPenayangan berubah
    LaunchedEffect(idPenayangan) {
        Log.d("DetailPenayangan", "Launching effect for idPenayangan: $idPenayangan")
        viewModel.getPenayanganById(idPenayangan)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(DestinasiDetailPenayangan.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getPenayanganById(idPenayangan) }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Penayangan")
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is DetailPenayanganUiState.Loading -> {
                    Log.d("DetailPenayangan", "UI State: Loading")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is DetailPenayanganUiState.Success -> {
                    Log.d("DetailPenayangan", "UI State: Success")
                    val penayangan = (uiState as DetailPenayanganUiState.Success).penayangan
                    ItemDetailPenayangan(penayangan = penayangan)
                }

                is DetailPenayanganUiState.Error -> {
                    Log.e("DetailPenayangan", "UI State: Error")
                    Text(
                        text = "Error: Gagal memuat data. Silakan coba lagi.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}


@Composable
fun ItemDetailPenayangan(
    modifier: Modifier = Modifier,
    penayangan: Penayangan
) {
    Log.d("DetailPenayangan", "Displaying data: $penayangan")

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailPenayangan(judul = "ID Penayangan", isinya = penayangan.idPenayangan.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailPenayangan(judul = "ID Film", isinya = penayangan.idFilm.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailPenayangan(judul = "ID Studio", isinya = penayangan.idStudio.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailPenayangan(judul = "Tanggal Penayangan", isinya = penayangan.tanggalPenayangan)
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailPenayangan(judul = "Harga Tiket", isinya = "Rp${penayangan.hargaTiket}")
        }
    }
}


@Composable
fun ComponentDetailPenayangan(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )
        )
        Text(
            text = isinya,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        )
    }
}
