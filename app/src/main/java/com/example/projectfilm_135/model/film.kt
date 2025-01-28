package com.example.projectfilm_135.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Film
)

@Serializable
data class FilmResponse(
    val status: Boolean,
    val message: String,
    val data: List<Film>
)

@Serializable
data class Film(
    @SerialName("id_film") val idFilm: Int,  // ID Film, menggunakan tipe data Int untuk id
    @SerialName("judul_film") val judulFilm: String,  // Judul Film
    val durasi: Int,  // Durasi Film dalam menit
    val deskripsi: String,  // Deskripsi Film
    val genre: String,  // Genre Film
    @SerialName("rating_usia")val ratingUsia: String  // Rating Usia Film (misalnya: "Dewasa", "Remaja")
)