package com.example.projectfilm_135.ui.viewmodel.penayangan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.repository.PenayanganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailPenayanganUiState {
    object Loading : DetailPenayanganUiState()
    data class Success(val penayangan: Penayangan) : DetailPenayanganUiState()
    object Error : DetailPenayanganUiState()
}

class DetailPenayanganViewModel(private val repository: PenayanganRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailPenayanganUiState>(DetailPenayanganUiState.Loading)
    val uiState: StateFlow<DetailPenayanganUiState> = _uiState

    fun getPenayanganById(idPenayangan: Int) {
        viewModelScope.launch {
            _uiState.value = DetailPenayanganUiState.Loading
            try {
                Log.d("DetailPenayangan", "Fetching data for idPenayangan: $idPenayangan")
                val penayangan = repository.getPenayanganById(idPenayangan)
                Log.d("DetailPenayangan", "Data fetched: $penayangan")
                _uiState.value = DetailPenayanganUiState.Success(penayangan)
            } catch (e: IOException) {
                Log.e("DetailPenayangan", "Network Error: ${e.message}")
                _uiState.value = DetailPenayanganUiState.Error
            } catch (e: HttpException) {
                Log.e("DetailPenayangan", "API Error: ${e.code()} ${e.message()}")
                _uiState.value = DetailPenayanganUiState.Error
            }
        }
    }
}

