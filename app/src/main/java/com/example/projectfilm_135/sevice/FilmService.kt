package com.example.projectfilm_135.sevice

import com.example.projectfilm_135.model.Film
import com.example.projectfilm_135.model.FilmDetailResponse
import com.example.projectfilm_135.model.FilmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface FilmService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacafilm.php")
    suspend fun getFilms(): List<Film>

    @GET("baca1film.php/{id_film}")
    suspend fun getFilmById(@Query("id_film") idFilm: Int):Film

    @POST("tambahfilm.php")
    suspend fun insertFilm(@Body film: Film)

    @PUT("updatefilm.php/{id_film}")
    suspend fun updateFilm(@Query("id_film") idFilm: Int, @Body film: Film)

    @DELETE("deletefilm.php/{id_film}")
    suspend fun deleteFilm(@Query("id_film") idFilm: Int): Response<Void>
}