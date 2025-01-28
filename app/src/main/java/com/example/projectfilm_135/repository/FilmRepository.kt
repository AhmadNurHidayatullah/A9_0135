package com.example.projectfilm_135.repository

import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.sevice.FilmService
import java.io.IOException

interface FilmRepository {
    suspend fun getFilms(): List<Film>
    suspend fun insertFilm(film: Film)
    suspend fun updateFilm(idFilm: Int, film: Film)
    suspend fun deleteFilm(idFilm: Int)
    suspend fun getFilmById(idFilm: Int): Film
}

class NetworkFilmRepository(
    private val filmAPIService: FilmService
): FilmRepository {

    override suspend fun insertFilm(film: Film) {
        filmAPIService.insertFilm(film)
    }

    override suspend fun updateFilm(idFilm: Int, film: Film) {
        filmAPIService.updateFilm(idFilm, film)
    }

    override suspend fun deleteFilm(idFilm: Int) {
        try {
            val response = filmAPIService.deleteFilm(idFilm)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete film. HTTP Status code: ${response.code()}")
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getFilms(): List<Film> = filmAPIService.getFilms()

    override suspend fun getFilmById(idFilm: Int): Film {
        return filmAPIService.getFilmById(idFilm)
    }
}