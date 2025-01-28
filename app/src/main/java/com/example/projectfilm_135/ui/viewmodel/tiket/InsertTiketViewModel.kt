package com.example.projectfilm_135.ui.viewmodel.tiket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.model.StatusPembayaran
import com.example.projectfilm_135.repository.PenayanganRepository
import com.example.projectfilm_135.repository.TiketRepository
import kotlinx.coroutines.launch

data class InsertTiketUiEvent(
    val idTiket: Int = 0,
    val idPenayangan: Int? = null,
    val judulFilm: String? = null,
    val studioNama: String? = null,
    val tanggalTayang: String? = null,
    val hargaTiket: Double? = null,
    val jumlahTiket: Int? = null,
    val totalHarga: Double? = null,
    val statusPembayaran: StatusPembayaran = StatusPembayaran.BELUM_LUNAS
)

data class InsertTiketUiState(
    val insertTiketUiEvent: InsertTiketUiEvent = InsertTiketUiEvent(),
    val penayanganList: List<Penayangan> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class InsertTiketViewModel(
    private val tiketRepository: TiketRepository,
    private val penayanganRepository: PenayanganRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertTiketUiState())
        private set

    init {
        loadPenayangan()
    }

    private fun loadPenayangan() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true)
                val penayangan = penayanganRepository.getPenayangan()
                uiState = uiState.copy(
                    penayanganList = penayangan,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun updateInsertTiketState(insertTiketUiEvent: InsertTiketUiEvent) {
        val jumlahTiket = insertTiketUiEvent.jumlahTiket ?: 0
        val hargaTiket = insertTiketUiEvent.hargaTiket ?: 0.0
        val totalHarga = jumlahTiket * hargaTiket

        uiState = uiState.copy(
            insertTiketUiEvent = insertTiketUiEvent.copy(
                totalHarga = totalHarga
            )
        )
    }

    fun updateJumlahTiket(jumlahTiket: Int) {
        val currentEvent = uiState.insertTiketUiEvent
        val hargaTiket = currentEvent.hargaTiket ?: 0.0
        val totalHarga = jumlahTiket * hargaTiket

        updateInsertTiketState(
            currentEvent.copy(
                jumlahTiket = jumlahTiket,
                totalHarga = totalHarga
            )
        )
    }

    fun updateIdPenayangan(idPenayangan: Int) {
        val penayangan = uiState.penayanganList.find { it.idPenayangan == idPenayangan }
        if (penayangan != null) {
            updateInsertTiketState(
                uiState.insertTiketUiEvent.copy(
                    idPenayangan = idPenayangan,
                    hargaTiket = penayangan.hargaTiket,
                    tanggalTayang = penayangan.tanggalPenayangan
                )
            )
        } else {
            updateInsertTiketState(
                uiState.insertTiketUiEvent.copy(
                    idPenayangan = idPenayangan,
                    hargaTiket = null,
                    tanggalTayang = null
                )
            )
        }
    }

    fun updateIdTiket(idTiket: Int) {
        val currentEvent = uiState.insertTiketUiEvent
        updateInsertTiketState(
            currentEvent.copy(idTiket = idTiket)
        )
    }

    fun updateStatusPembayaran(status: StatusPembayaran) {
        updateInsertTiketState(
            uiState.insertTiketUiEvent.copy(
                statusPembayaran = status
            )
        )
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    suspend fun insertTiket() {
        val event = uiState.insertTiketUiEvent
        if (validateInput(event)) {
            try {
                tiketRepository.insertTiket(
                    Tiket(
                        idTiket = event.idTiket,
                        idPenayangan = event.idPenayangan!!,
                        jumlahTiket = event.jumlahTiket!!,
                        totalHarga = event.totalHarga!!,
                        statusPembayaran = event.statusPembayaran
                    )
                )
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message)
            }
        }
    }

    private fun validateInput(event: InsertTiketUiEvent): Boolean {
        if (event.idPenayangan == null || event.hargaTiket == null) {
            uiState = uiState.copy(error = "ID Penayangan tidak valid atau tidak ditemukan.")
            return false
        }
        if (event.jumlahTiket == null || event.jumlahTiket <= 0) {
            uiState = uiState.copy(error = "Jumlah tiket harus lebih dari 0.")
            return false
        }
        return true
    }

    fun resetState() {
        uiState = InsertTiketUiState()
    }
}

