package com.example.projectfilm_135.ui.viewmodel.tiket


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectfilm_135.FilmApplication
import com.example.projectfilm_135.ui.viewmodel.penayangan.InsertPenayanganViewModel
import com.example.projectfilm_135.ui.viewmodel.penayangan.PenayanganApp

object PenyediaTiketViewModel {
    val Factory = viewModelFactory {
        // Inisialisasi HomeTiketViewModel
        initializer { HomeTiketViewModel(TiketApp().container.tiketRepository) }
        initializer {
            InsertTiketViewModel(
                tiketRepository = PenayanganApp().container.tiketRepository,
                penayanganRepository = PenayanganApp().container.penayanganRepository
            )
        }
        initializer { DetailTiketViewModel(TiketApp().container.tiketRepository) }
    }
}

fun CreationExtras.TiketApp(): FilmApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FilmApplication)
