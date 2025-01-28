package com.example.projectfilm_135.ui.viewmodel.film

import android.net.http.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.repository.FilmRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState {
    data class Success(val film: List<Film>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

// ViewModel untuk Film
class FilmViewModel(private val film: FilmRepository) : ViewModel() {
    var filmUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getFilms()
    }

    // Mendapatkan data film dari repository
    fun getFilms() {
        viewModelScope.launch {
            filmUiState = HomeUiState.Loading
            filmUiState = try {
                HomeUiState.Success(film.getFilms())
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: retrofit2.HttpException) {
                HomeUiState.Error
            }
        }
    }

    // Menghapus film berdasarkan ID
    fun deleteFilm(idFilm: Int) {
        viewModelScope.launch {
            try {
                film.deleteFilm(idFilm)
                // Memperbarui daftar film setelah penghapusan
                getFilms()
            } catch (e: IOException) {
                filmUiState = HomeUiState.Error
            } catch (e: retrofit2.HttpException) {
                filmUiState = HomeUiState.Error
            }
        }
    }
}