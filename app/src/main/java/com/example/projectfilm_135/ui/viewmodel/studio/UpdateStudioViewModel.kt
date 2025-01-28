package com.example.projectfilm_135.ui.viewmodel.studio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.repository.StudioRepository
import kotlinx.coroutines.launch

sealed class UpdtStudioUIState {
    data class Success(val studio: List<Studio>) : UpdtStudioUIState()
    object Error : UpdtStudioUIState()
    object Loading : UpdtStudioUIState()
}

class UpdateStudioViewModel(private val studioRepository: StudioRepository) : ViewModel() {

    var updateStudioUiState by mutableStateOf(UpdateStudioUiState())
        private set

    fun getStudioById(idStudio: Int) {
        viewModelScope.launch {
            try {
                val studio = studioRepository.getStudioById(idStudio)
                updateStudioUiState = updateStudioUiState.copy(
                    updateStudioUiEvent = UpdateStudioUiEvent(
                        idStudio = studio.idStudio,
                        namaStudio = studio.namaStudio,
                        kapasitas = studio.kapasitas
                    )
                )
            } catch (e: Exception) {
                UpdtStudioUIState.Error
            }
        }
    }

    fun updateStudioState(updateStudioUiEvent: UpdateStudioUiEvent) {
        updateStudioUiState = updateStudioUiState.copy(updateStudioUiEvent = updateStudioUiEvent)
    }

    fun loadStudio(studio: Studio) {
        updateStudioUiState = studio.toUpdateStudioUiState()
    }

    fun updateStudio() {
        viewModelScope.launch {
            try {
                studioRepository.updateStudio(
                    idStudio = updateStudioUiState.updateStudioUiEvent.idStudio,
                    studio = updateStudioUiState.updateStudioUiEvent.toStudio()
                )
            } catch (e: Exception) {
                UpdtStudioUIState.Error
            }
        }
    }
}

data class UpdateStudioUiState(
    val updateStudioUiEvent: UpdateStudioUiEvent = UpdateStudioUiEvent()
)

data class UpdateStudioUiEvent(
    val idStudio: Int = 0,
    val namaStudio: String = "",
    val kapasitas: Int = 0
)

fun UpdateStudioUiEvent.toStudio(): Studio = Studio(
    idStudio = idStudio,
    namaStudio = namaStudio,
    kapasitas = kapasitas
)

fun Studio.toUpdateStudioUiState(): UpdateStudioUiState = UpdateStudioUiState(
    updateStudioUiEvent = toUpdateStudioUiEvent()
)

fun Studio.toUpdateStudioUiEvent(): UpdateStudioUiEvent = UpdateStudioUiEvent(
    idStudio = idStudio,
    namaStudio = namaStudio,
    kapasitas = kapasitas
)
