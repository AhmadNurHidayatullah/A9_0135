package com.example.projectfilm_135.ui.viewmodel.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailFilmUiState {
    object Loading : DetailFilmUiState()
    data class Success(val film: Film) : DetailFilmUiState()
    object Error : DetailFilmUiState()
}

class DetailFilmViewModel(private val repository: FilmRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailFilmUiState>(DetailFilmUiState.Loading)
    val uiState: StateFlow<DetailFilmUiState> = _uiState

    fun getFilmById(idFilm: Int) {
        viewModelScope.launch {
            _uiState.value = DetailFilmUiState.Loading
            try {
                val film = repository.getFilmById(idFilm)
                _uiState.value = DetailFilmUiState.Success(film)
            } catch (e: IOException) {
                e.printStackTrace()
                _uiState.value = DetailFilmUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                _uiState.value = DetailFilmUiState.Error
            }
        }
    }
}
