package com.example.projectfilm_135.ui.viewmodel.studio

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.repository.StudioRepository
import kotlinx.coroutines.launch

class InsertStudioViewModel(private val studioRepository: StudioRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertStudioUiState())
        private set

    // Memperbarui state berdasarkan input pengguna
    fun updateInsertStudioState(insertStudioUiEvent: InsertStudioUiEvent) {
        uiState = InsertStudioUiState(insertStudioUiEvent = insertStudioUiEvent)
    }

    // Fungsi untuk menyisipkan data studio
    fun insertStudio() {
        viewModelScope.launch {
            try {
                studioRepository.insertStudio(uiState.insertStudioUiEvent.toStudio())
                Log.d("InsertStudio", "Data studio berhasil ditambahkan")
            } catch (e: Exception) {
                Log.e("InsertStudio", "Gagal menambahkan data studio", e)
            }
        }
    }
}

// Data class untuk mengelola UI state
data class InsertStudioUiState(
    val insertStudioUiEvent: InsertStudioUiEvent = InsertStudioUiEvent()
)

// Data class untuk event input dari pengguna
data class InsertStudioUiEvent(
    val idStudio: Int = 0,
    val namaStudio: String = "",
    val kapasitas: Int = 0
)

// Konversi dari `InsertStudioUiEvent` ke `Studio`
fun InsertStudioUiEvent.toStudio(): Studio = Studio(
    idStudio = idStudio,
    namaStudio = namaStudio,
    kapasitas = kapasitas
)

// Konversi dari `Studio` ke `InsertStudioUiState`
fun Studio.toUiStateStudio(): InsertStudioUiState = InsertStudioUiState(
    insertStudioUiEvent = toInsertStudioUiEvent()
)

// Konversi dari `Studio` ke `InsertStudioUiEvent`
fun Studio.toInsertStudioUiEvent(): InsertStudioUiEvent = InsertStudioUiEvent(
    idStudio = idStudio,
    namaStudio = namaStudio,
    kapasitas = kapasitas
)