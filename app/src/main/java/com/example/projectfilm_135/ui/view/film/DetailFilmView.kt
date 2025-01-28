package com.example.projectfilm_135.ui.view.film

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
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
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.film.DetailFilmUiState
import com.example.projectfilm_135.ui.viewmodel.film.DetailFilmViewModel
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel

object DestinasiDetailFilm : DestinasiNavigasi {
    override val route = "detail_film"
    override val titleRes = "Detail Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFilmView(
    idFilm: Int,
    navigateBack: () -> Unit,
    onClick: () -> Unit,
    viewModel: DetailFilmViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Fetch the film data only when the idFilm changes
    LaunchedEffect(idFilm) {
        viewModel.getFilmById(idFilm)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(DestinasiDetailFilm.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getFilmById(idFilm) }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Film")
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is DetailFilmUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is DetailFilmUiState.Success -> {
                    val film = (uiState as DetailFilmUiState.Success).film
                    ItemDetailFilm(film = film)
                }

                is DetailFilmUiState.Error -> {
                    Text(
                        text = "Error: Gagal memuat data. Silakan coba lagi.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ItemDetailFilm(
    modifier: Modifier = Modifier,
    film: Film
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp), // More rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp) // Higher elevation for better visual effect
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            ComponentDetailFilm(judul = "ID Film", isinya = film.idFilm.toString())
            Spacer(modifier = Modifier.height(12.dp)) // Adjusted space for balance

            ComponentDetailFilm(judul = "Judul Film", isinya = film.judulFilm)
            Spacer(modifier = Modifier.height(12.dp))

            ComponentDetailFilm(judul = "Durasi", isinya = "${film.durasi} menit")
            Spacer(modifier = Modifier.height(12.dp))

            ComponentDetailFilm(judul = "Deskripsi", isinya = film.deskripsi)
            Spacer(modifier = Modifier.height(12.dp))

            ComponentDetailFilm(judul = "Genre", isinya = film.genre)
            Spacer(modifier = Modifier.height(12.dp))

            ComponentDetailFilm(judul = "Rating Usia", isinya = film.ratingUsia)
        }
    }
}

@Composable
fun ComponentDetailFilm(
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
                fontSize = 20.sp // Bigger font for titles
            )
        )
        Text(
            text = isinya,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp // Consistent font size for values
            )
        )
    }
}
