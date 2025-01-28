package com.example.projectfilm_135.ui.viewmodel.penayangan

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectfilm_135.FilmApplication

object PenyediaPenayanganViewModel {
    val Factory = viewModelFactory {
        // Inisialisasi HomePenayanganViewModel
        initializer { HomePenayanganViewModel(PenayanganApp().container.penayanganRepository) }
        initializer {
            InsertPenayanganViewModel(
                penayanganRepository = PenayanganApp().container.penayanganRepository,
                filmRepository = PenayanganApp().container.filmRepository,
                studioRepository = PenayanganApp().container.studioRepository
            )
        }
        initializer { DetailPenayanganViewModel(PenayanganApp().container.penayanganRepository)}
        initializer { UpdatePenayanganViewModel(PenayanganApp().container.penayanganRepository) }
    }
}

fun CreationExtras.PenayanganApp(): FilmApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FilmApplication)
