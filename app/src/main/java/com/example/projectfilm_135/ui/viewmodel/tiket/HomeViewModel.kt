package com.example.projectfilm_135.ui.viewmodel.tiket


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.repository.TiketRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeTiketUiState {
    data class Success(val tiketList: List<Tiket>) : HomeTiketUiState()
    object Error : HomeTiketUiState()
    object Loading : HomeTiketUiState()
}

// ViewModel untuk Tiket
class HomeTiketViewModel(private val tiketRepository: TiketRepository) : ViewModel() {
    var tiketUiState: HomeTiketUiState by mutableStateOf(HomeTiketUiState.Loading)
        private set

    init {
        getTikets()
    }

    // Mendapatkan data tiket dari repository
    fun getTikets() {
        viewModelScope.launch {
            tiketUiState = HomeTiketUiState.Loading
            tiketUiState = try {
                HomeTiketUiState.Success(tiketRepository.getTikets())
            } catch (e: IOException) {
                HomeTiketUiState.Error
            } catch (e: retrofit2.HttpException) {
                HomeTiketUiState.Error
            }
        }
    }

    // Menghapus tiket berdasarkan ID
    fun deleteTiket(idTiket: Int) {
        viewModelScope.launch {
            try {
                tiketRepository.deleteTiket(idTiket)
                // Memperbarui daftar tiket setelah penghapusan
                getTikets()
            } catch (e: IOException) {
                tiketUiState = HomeTiketUiState.Error
            } catch (e: retrofit2.HttpException) {
                tiketUiState = HomeTiketUiState.Error
            }
        }
    }
}
