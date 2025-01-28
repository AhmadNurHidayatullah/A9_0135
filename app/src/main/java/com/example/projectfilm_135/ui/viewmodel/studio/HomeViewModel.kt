package com.example.projectfilm_135.ui.viewmodel.studio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.repository.StudioRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeStudioUiState {
    data class Success(val studio: List<Studio>) : HomeStudioUiState()
    object Error : HomeStudioUiState()
    object Loading : HomeStudioUiState()
}

// ViewModel untuk Studio
class StudioViewModel(private val studioRepository: StudioRepository) : ViewModel() {
    var studioUiState: HomeStudioUiState by mutableStateOf(HomeStudioUiState.Loading)
        private set

    init {
        getStudios()
    }

    // Mendapatkan data studio dari repository
    fun getStudios() {
        viewModelScope.launch {
            studioUiState = HomeStudioUiState.Loading
            studioUiState = try {
                HomeStudioUiState.Success(studioRepository.getStudios())
            } catch (e: IOException) {
                HomeStudioUiState.Error
            } catch (e: retrofit2.HttpException) {
                HomeStudioUiState.Error
            }
        }
    }

    // Menghapus studio berdasarkan ID
    fun deleteStudio(idStudio: Int) {
        viewModelScope.launch {
            try {
                studioRepository.deleteStudio(idStudio)
                // Memperbarui daftar studio setelah penghapusan
                getStudios()
            } catch (e: IOException) {
                studioUiState = HomeStudioUiState.Error
            } catch (e: retrofit2.HttpException) {
                studioUiState = HomeStudioUiState.Error
            }
        }
    }
}
