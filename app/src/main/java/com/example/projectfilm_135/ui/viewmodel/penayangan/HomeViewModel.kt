package com.example.projectfilm_135.ui.viewmodel.penayangan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.repository.PenayanganRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomePenayanganUiState {
    data class Success(val penayanganList: List<Penayangan>) : HomePenayanganUiState()
    object Error : HomePenayanganUiState()
    object Loading : HomePenayanganUiState()
}

// ViewModel untuk Penayangan
class HomePenayanganViewModel(private val Penayanganrepository: PenayanganRepository) : ViewModel() {
    var penayanganUiState: HomePenayanganUiState by mutableStateOf(HomePenayanganUiState.Loading)
        private set

    init {
        getPenayangan()
    }

    // Mendapatkan data penayangan dari repository
    fun getPenayangan() {
        viewModelScope.launch {
            penayanganUiState = HomePenayanganUiState.Loading
            penayanganUiState = try {
                HomePenayanganUiState.Success(Penayanganrepository.getPenayangan())
            } catch (e: IOException) {
                HomePenayanganUiState.Error
            } catch (e: retrofit2.HttpException) {
                HomePenayanganUiState.Error
            }
        }
    }

    // Menghapus penayangan berdasarkan ID
    fun deletePenayangan(idPenayangan: Int) {
        viewModelScope.launch {
            try {
                Penayanganrepository.deletePenayangan(idPenayangan)
                // Memperbarui daftar penayangan setelah penghapusan
                getPenayangan()
            } catch (e: IOException) {
                penayanganUiState = HomePenayanganUiState.Error
            } catch (e: retrofit2.HttpException) {
                penayanganUiState = HomePenayanganUiState.Error
            }
        }
    }
}
