package com.example.projectfilm_135.ui.viewmodel.penayangan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.repository.PenayanganRepository
import kotlinx.coroutines.launch

sealed class UpdtPenayanganUIState {
    data class Success(val penayangan: List<Penayangan>) : UpdtPenayanganUIState()
    object Error : UpdtPenayanganUIState()
    object Loading : UpdtPenayanganUIState()
}

class UpdatePenayanganViewModel(
    private val penayanganRepository: PenayanganRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdatePenayanganUiState())
        private set

    fun loadPenayanganById(idPenayangan: Int) {
        viewModelScope.launch {
            try {
                val penayangan = penayanganRepository.getPenayanganById(idPenayangan)
                uiState = uiState.copy(
                    updatePenayanganUiEvent = UpdatePenayanganUiEvent(
                        idPenayangan = penayangan.idPenayangan,
                        idFilm = penayangan.idFilm,
                        idStudio = penayangan.idStudio,
                        tanggalPenayangan = penayangan.tanggalPenayangan,
                        hargaTiket = penayangan.hargaTiket
                    )
                )
            } catch (e: Exception) {
                // Error handling jika data gagal di-load
                println("Error loading Penayangan: ${e.message}")
            }
        }
    }

    fun updatePenayanganState(updatePenayanganUiEvent: UpdatePenayanganUiEvent) {
        uiState = uiState.copy(updatePenayanganUiEvent = updatePenayanganUiEvent)
    }

    fun saveUpdatedPenayangan() {
        val event = uiState.updatePenayanganUiEvent
        if (validateInput(event)) {
            viewModelScope.launch {
                try {
                    penayanganRepository.updatePenayangan(
                        idPenayangan = event.idPenayangan,
                        penayangan = Penayangan(
                            idPenayangan = event.idPenayangan,
                            idFilm = event.idFilm,
                            idStudio = event.idStudio,
                            tanggalPenayangan = event.tanggalPenayangan,
                            hargaTiket = event.hargaTiket
                        )
                    )
                } catch (e: Exception) {
                    // Error handling jika update gagal
                    println("Error updating Penayangan: ${e.message}")
                }
            }
        }
    }

    private fun validateInput(event: UpdatePenayanganUiEvent): Boolean {
        return event.idFilm > 0 &&
                event.idStudio > 0 &&
                event.tanggalPenayangan.isNotEmpty() &&
                event.hargaTiket > 0
    }
}

data class UpdatePenayanganUiState(
    val updatePenayanganUiEvent: UpdatePenayanganUiEvent = UpdatePenayanganUiEvent()
)

data class UpdatePenayanganUiEvent(
    val idPenayangan: Int = 0,
    val idFilm: Int = 0,
    val idStudio: Int = 0,
    val tanggalPenayangan: String = "", // Menggunakan format String untuk tanggal
    val hargaTiket: Double = 0.0 // Harga tiket dalam format Decimal
)

fun UpdatePenayanganUiEvent.toPenayangan(): Penayangan = Penayangan(
    idPenayangan = idPenayangan,
    idFilm = idFilm,
    idStudio = idStudio,
    tanggalPenayangan = tanggalPenayangan,
    hargaTiket = hargaTiket
)

fun Penayangan.toUpdatePenayanganUiState(): UpdatePenayanganUiState = UpdatePenayanganUiState(
    updatePenayanganUiEvent = toUpdatePenayanganUiEvent()
)

fun Penayangan.toUpdatePenayanganUiEvent(): UpdatePenayanganUiEvent = UpdatePenayanganUiEvent(
    idPenayangan = idPenayangan,
    idFilm = idFilm,
    idStudio = idStudio,
    tanggalPenayangan = tanggalPenayangan,
    hargaTiket = hargaTiket
)
