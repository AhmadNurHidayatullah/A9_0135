package com.example.projectfilm_135.ui.view.film

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectfilm_135.R
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.ui.customewidget.TopAppBar
import com.example.projectfilm_135.ui.navigation.DestinasiNavigasi
import com.example.projectfilm_135.ui.viewmodel.film.FilmViewModel
import com.example.projectfilm_135.ui.viewmodel.film.HomeUiState
import com.example.projectfilm_135.ui.viewmodel.film.PenyediaViewModel


object DestinasiHomeFilm : DestinasiNavigasi {
    override val route = "home_film"
    override val titleRes = "Daftar Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: FilmViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showDialog = remember { mutableStateOf(false) }
    val filmToDelete = remember { mutableStateOf<Film?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = DestinasiHomeFilm.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getFilms()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Film")
            }
        },
    ) { innerPadding ->
        FilmStatus(
            homeUiState = viewModel.filmUiState,
            retryAction = { viewModel.getFilms() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                filmToDelete.value = it
                showDialog.value = true
            }
        )

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Apakah Anda yakin ingin menghapus film ini?") },
                confirmButton = {
                    Button(onClick = {
                        filmToDelete.value?.let { film ->
                            viewModel.deleteFilm(film.idFilm)
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

@Composable
fun FilmCard(
    film: Film,
    modifier: Modifier = Modifier,
    onDeleteClick: (Film) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = MaterialTheme.shapes.large, // Rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Soft shadow
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Soft background color
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing for clarity
        ) {
            // Title and Delete Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = film.judulFilm,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // Bold title
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDeleteClick(film) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Film details
            Text(
                text = "Durasi: ${film.durasi} menit",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Text(
                text = "Genre: ${film.genre}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "",
            modifier = Modifier.size(100.dp) // Adjust size for better visibility
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Button(
            onClick = retryAction,
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.medium // Rounded corners for button

        ) {
            Text(
                text = stringResource(R.string.retry),
                color = MaterialTheme.colorScheme.onPrimary // Contrasting text color for button
            )
        }
    }
}

@Composable
fun FilmStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Film) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success -> {
            if (homeUiState.film.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak ada data film",

                    )
                }
            } else {
                FilmLayout(
                    film = homeUiState.film,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.idFilm.toString()) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun FilmLayout(
    film: List<Film>,
    modifier: Modifier = Modifier,
    onDetailClick: (Film) -> Unit,
    onDeleteClick: (Film) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Adjust space between cards
    ) {
        items(film) { film ->
            FilmCard(
                film = film,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(film) },
                onDeleteClick = { onDeleteClick(film) }
            )
        }
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading),
        contentDescription = stringResource(R.string.loading)
    )
}
