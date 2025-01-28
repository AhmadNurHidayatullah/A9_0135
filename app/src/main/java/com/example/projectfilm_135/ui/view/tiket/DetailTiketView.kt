package com.example.projectfilm_135.ui.view.tiket


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
import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.tiket.DetailTiketUiState
import com.example.projectfilm_135.ui.viewmodel.tiket.DetailTiketViewModel
import com.example.projectfilm_135.ui.viewmodel.tiket.PenyediaTiketViewModel

object DestinasiDetailTiket : DestinasiNavigasi {
    override val route = "detail_tiket"
    override val titleRes = "Detail Tiket"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTiketView(
    idTiket: Int,
    navigateBack: () -> Unit,
    onClick: () -> Unit,
    viewModel: DetailTiketViewModel = viewModel(factory = PenyediaTiketViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Fetch data saat idTiket berubah
    LaunchedEffect(idTiket) {
        Log.d("DetailTiket", "Launching effect for idTiket: $idTiket")
        viewModel.getTiketById(idTiket)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(DestinasiDetailTiket.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getTiketById(idTiket) }) {
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
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Tiket")
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is DetailTiketUiState.Loading -> {
                    Log.d("DetailTiket", "UI State: Loading")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is DetailTiketUiState.Success -> {
                    Log.d("DetailTiket", "UI State: Success")
                    val tiket = (uiState as DetailTiketUiState.Success).tiket
                    ItemDetailTiket(tiket = tiket)
                }

                is DetailTiketUiState.Error -> {
                    Log.e("DetailTiket", "UI State: Error")
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
fun ItemDetailTiket(
    modifier: Modifier = Modifier,
    tiket: Tiket
) {
    Log.d("DetailTiket", "Displaying data: $tiket")

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
            ComponentDetailTiket(judul = "ID Tiket", isinya = tiket.idTiket.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailTiket(judul = "ID Penayangan", isinya = tiket.idPenayangan.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailTiket(judul = "Jumlah Tiket", isinya = tiket.jumlahTiket.toString())
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailTiket(judul = "Total Harga", isinya = "Rp${tiket.totalHarga}")
            Spacer(modifier = Modifier.height(8.dp))

            ComponentDetailTiket(judul = "Status Pembayaran", isinya = tiket.statusPembayaran.name)
        }
    }
}

@Composable
fun ComponentDetailTiket(
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
