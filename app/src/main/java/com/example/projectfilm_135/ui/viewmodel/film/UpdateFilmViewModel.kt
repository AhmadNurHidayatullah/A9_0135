package com.example.projectfilm_135.ui.viewmodel.film

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.repository.FilmRepository
import kotlinx.coroutines.launch

sealed class UpdtFilmUIState {
    data class Success(val film: List<Film>) : UpdtFilmUIState()
    object Error : UpdtFilmUIState()
    object Loading : UpdtFilmUIState()
}

class UpdateFilmViewModel (private val filmRepository: FilmRepository) : ViewModel() {

    var updateFilmUiState by mutableStateOf(UpdateFilmUiState())
        private set

    fun getFilmById(idFilm: Int) {
        viewModelScope.launch {
            try {
                val film = filmRepository.getFilmById(idFilm)
                updateFilmUiState = updateFilmUiState.copy(
                    updateFilmUiEvent = UpdateFilmUiEvent(
                        idFilm = film.idFilm,
                        judulFilm = film.judulFilm,
                        durasi = film.durasi,
                        deskripsi = film.deskripsi,
                        genre = film.genre,
                        ratingUsia = film.ratingUsia
                    )
                )
            } catch (e: Exception) {
                UpdtFilmUIState.Error
            }
        }
    }

    fun updateFilmState(updateFilmUiEvent: UpdateFilmUiEvent) {
        updateFilmUiState = updateFilmUiState.copy(updateFilmUiEvent = updateFilmUiEvent)
    }

    fun loadFilm(film: Film) {
        updateFilmUiState = film.toUpdateFilmUiState()
    }

    fun updateFilm() {
        viewModelScope.launch {
            try {
                filmRepository.updateFilm(
                    idFilm = updateFilmUiState.updateFilmUiEvent.idFilm,
                    film = updateFilmUiState.updateFilmUiEvent.toFilm()
                )
            } catch (e: Exception) {
                UpdtFilmUIState.Error
            }
        }
    }
}

data class UpdateFilmUiState(
    val updateFilmUiEvent: UpdateFilmUiEvent = UpdateFilmUiEvent(),

)

data class UpdateFilmUiEvent(
    val idFilm: Int = 0,
    val judulFilm: String = "",
    val durasi: Int = 0,
    val deskripsi: String = "",
    val genre: String = "",
    val ratingUsia: String = ""
)

fun UpdateFilmUiEvent.toFilm(): Film = Film(
    idFilm = idFilm,
    judulFilm = judulFilm,
    durasi = durasi,
    deskripsi = deskripsi,
    genre = genre,
    ratingUsia = ratingUsia
)

fun Film.toUpdateFilmUiState(): UpdateFilmUiState = UpdateFilmUiState(
    updateFilmUiEvent = toUpdateFilmUiEvent()
)

fun Film.toUpdateFilmUiEvent(): UpdateFilmUiEvent = UpdateFilmUiEvent(
    idFilm = idFilm,
    judulFilm = judulFilm,
    durasi = durasi,
    deskripsi = deskripsi,
    genre = genre,
    ratingUsia = ratingUsia
)



