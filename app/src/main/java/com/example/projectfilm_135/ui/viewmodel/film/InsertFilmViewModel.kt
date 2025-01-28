package com.example.projectfilm_135.ui.viewmodel.film

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.repository.FilmRepository
import kotlinx.coroutines.launch

class InsertFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertFilmUiState())
        private set

    // Memperbarui state berdasarkan input pengguna
    fun updateInsertFilmState(insertFilmUiEvent: InsertFilmUiEvent) {
        uiState = InsertFilmUiState(insertFilmUiEvent = insertFilmUiEvent)
    }

    // Fungsi untuk menyisipkan data film
    fun insertFilm() {
        viewModelScope.launch {
            try {
                filmRepository.insertFilm(uiState.insertFilmUiEvent.toFilm())
                Log.d("InsertFilm", "Data berhasil ditambahkan")
            } catch (e: Exception) {
                Log.e("InsertFilm", "Gagal menambahkan data", e)
            }
        }
    }
}

// Data class untuk mengelola UI state
data class InsertFilmUiState(
    val insertFilmUiEvent: InsertFilmUiEvent = InsertFilmUiEvent()
)

// Data class untuk event input dari pengguna
data class InsertFilmUiEvent(
    val idFilm: Int = 0,
    val judulFilm: String = "",
    val durasi: Int = 0,
    val deskripsi: String = "",
    val genre: String = "",
    val ratingUsia: String = ""
)

// Konversi dari `InsertFilmUiEvent` ke `Film`
fun InsertFilmUiEvent.toFilm(): Film = Film(
    idFilm = idFilm,
    judulFilm = judulFilm,
    durasi = durasi,
    deskripsi = deskripsi,
    genre = genre,
    ratingUsia = ratingUsia
)

// Konversi dari `Film` ke `InsertFilmUiState`
fun Film.toUiStateFilm(): InsertFilmUiState = InsertFilmUiState(
    insertFilmUiEvent = toInsertFilmUiEvent()
)

// Konversi dari `Film` ke `InsertFilmUiEvent`
fun Film.toInsertFilmUiEvent(): InsertFilmUiEvent = InsertFilmUiEvent(
    idFilm = idFilm,
    judulFilm = judulFilm,
    durasi = durasi,
    deskripsi = deskripsi,
    genre = genre,
    ratingUsia = ratingUsia
)