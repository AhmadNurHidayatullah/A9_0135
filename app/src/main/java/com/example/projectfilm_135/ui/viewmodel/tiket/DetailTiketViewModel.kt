package com.example.projectfilm_135.ui.viewmodel.tiket


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.repository.TiketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailTiketUiState {
    object Loading : DetailTiketUiState()
    data class Success(val tiket: Tiket) : DetailTiketUiState()
    object Error : DetailTiketUiState()
}

class DetailTiketViewModel(private val repository: TiketRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailTiketUiState>(DetailTiketUiState.Loading)
    val uiState: StateFlow<DetailTiketUiState> = _uiState

    fun getTiketById(idTiket: Int) {
        viewModelScope.launch {
            _uiState.value = DetailTiketUiState.Loading
            try {
                Log.d("DetailTiket", "Fetching data for idTiket: $idTiket")
                val tiket = repository.getTiketById(idTiket)
                Log.d("DetailTiket", "Data fetched: $tiket")
                _uiState.value = DetailTiketUiState.Success(tiket)
            } catch (e: IOException) {
                Log.e("DetailTiket", "Network Error: ${e.message}")
                _uiState.value = DetailTiketUiState.Error
            } catch (e: HttpException) {
                Log.e("DetailTiket", "API Error: ${e.code()} ${e.message()}")
                _uiState.value = DetailTiketUiState.Error
            }
        }
    }
}
