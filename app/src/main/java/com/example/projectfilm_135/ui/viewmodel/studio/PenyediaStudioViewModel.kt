package com.example.projectfilm_135.ui.viewmodel.studio

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectfilm_135.FilmApplication
import com.example.projectfilm_135.ui.viewmodel.film.FilmViewModel


object PenyediaStudioViewModel {
    val Factory = viewModelFactory {
        // Inisialisasi StudioViewModel
        initializer { StudioViewModel(StudioApp().container.studioRepository) }
        initializer { InsertStudioViewModel(StudioApp().container.studioRepository) }
        initializer { DetailStudioViewModel(StudioApp().container.studioRepository)}
        initializer { UpdateStudioViewModel(StudioApp().container.studioRepository) }
    }
}

fun CreationExtras.StudioApp(): FilmApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FilmApplication)
