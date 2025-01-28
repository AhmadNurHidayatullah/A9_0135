package com.example.projectfilm_135.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Studio(
    @SerialName("id_studio") val idStudio: Int,  // ID Film, menggunakan tipe data Int untuk id
    @SerialName("nama_studio") val namaStudio: String,  // Judul Film
    val kapasitas: Int,  // Durasi Film dalam menit
)