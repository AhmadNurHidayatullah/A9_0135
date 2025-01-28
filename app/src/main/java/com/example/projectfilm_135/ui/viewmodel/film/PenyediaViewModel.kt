package com.example.projectfilm_135.ui.viewmodel.film

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectfilm_135.FilmApplication


object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Inisialisasi FilmViewModel
        initializer { FilmViewModel(FilmApp().container.filmRepository) }
        initializer { InsertFilmViewModel(FilmApp().container.filmRepository) }
        initializer { DetailFilmViewModel(FilmApp().container.filmRepository) }
        initializer { UpdateFilmViewModel(FilmApp().container.filmRepository) }
    }
}

fun CreationExtras.FilmApp(): FilmApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FilmApplication)
