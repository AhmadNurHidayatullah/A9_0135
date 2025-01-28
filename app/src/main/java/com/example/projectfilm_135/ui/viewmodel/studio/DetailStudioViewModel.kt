package com.example.projectfilm_135.ui.viewmodel.studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.repository.StudioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailStudioUiState {
    object Loading : DetailStudioUiState()
    data class Success(val studio: Studio) : DetailStudioUiState()
    object Error : DetailStudioUiState()
}

class DetailStudioViewModel(private val repositorystudio: StudioRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailStudioUiState>(DetailStudioUiState.Loading)
    val uiState: StateFlow<DetailStudioUiState> = _uiState

    fun getStudioById(idStudio: Int) {
        viewModelScope.launch {
            _uiState.value = DetailStudioUiState.Loading
            try {
                val studio = repositorystudio.getStudioById(idStudio)
                _uiState.value = DetailStudioUiState.Success(studio)
            } catch (e: IOException) {
                e.printStackTrace()
                _uiState.value = DetailStudioUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                _uiState.value = DetailStudioUiState.Error
            }
        }
    }
}
