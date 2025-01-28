package com.example.projectfilm_135.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Penayangan(
    @SerialName("id_penayangan") val idPenayangan: Int,  // ID Penayangan sebagai Primary Key
    @SerialName("id_film") val idFilm: Int,  // Foreign Key ke tabel Film
    @SerialName("id_studio") val idStudio: Int,  // Foreign Key ke tabel Studio
    @SerialName("tanggal_penayangan") val tanggalPenayangan: String,  // Gunakan serializer kontekstual
    @SerialName("harga_tiket") val hargaTiket: Double  // Harga Tiket dalam format Decimal(10,2)
)