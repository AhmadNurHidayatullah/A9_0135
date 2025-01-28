package com.example.projectfilm_135.ui.viewmodel.penayangan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.repository.FilmRepository
import com.example.projectfilm_135.repository.PenayanganRepository
import com.example.projectfilm_135.repository.StudioRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class Penayangan(
    val idPenayangan: Int = 0,
    val idFilm: Int,
    val idStudio: Int,
    val tanggalPenayangan: String,
    val hargaTiket: Double
)

data class InsertPenayanganUiEvent(
    val idPenayangan: Int = 0,
    val idFilm: Int? = null,
    val namaFilm: String? = null,
    val idStudio: Int? = null,
    val namaStudio: String? = null,
    val tanggalPenayangan: String? = null,
    val hargaTiket: Double? = null
)

data class InsertPenayanganUiState(
    val insertPenayanganUiEvent: InsertPenayanganUiEvent = InsertPenayanganUiEvent(),
    val filmList: List<Film> = listOf(),
    val studioList: List<Studio> = listOf()
)

class InsertPenayanganViewModel(
    private val penayanganRepository: PenayanganRepository,
    private val filmRepository: FilmRepository,
    private val studioRepository: StudioRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertPenayanganUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                filmList = filmRepository.getFilms(),
                studioList = studioRepository.getStudios()
            )
        }
    }

    fun updateInsertPenayanganState(insertPenayanganUiEvent: InsertPenayanganUiEvent) {
        uiState = uiState.copy(insertPenayanganUiEvent = insertPenayanganUiEvent)
    }

    suspend fun insertPenayangan() {
        val event = uiState.insertPenayanganUiEvent
        if (validateInput(event)) {
            penayanganRepository.insertPenayangan(
                com.example.projectfilm_135.model.Penayangan(
                    idPenayangan = event.idPenayangan,
                    idFilm = event.idFilm!!,
                    idStudio = event.idStudio!!,
                    tanggalPenayangan = event.tanggalPenayangan!!.toString(),
                    hargaTiket = event.hargaTiket!!
                )
            )
        }
    }

    private fun validateInput(event: InsertPenayanganUiEvent): Boolean {
        return event.idFilm != null &&
                event.idStudio != null &&
                event.tanggalPenayangan != null &&
                event.hargaTiket != null
    }

}