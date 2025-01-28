package com.example.projectfilm_135.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tiket(
    @SerialName("id_tiket") val idTiket: Int,  // ID Tiket sebagai Primary Key
    @SerialName("id_penayangan") val idPenayangan: Int,  // Foreign Key dari tabel Penayangan
    @SerialName("jumlah_tiket") val jumlahTiket: Int,  // Jumlah tiket
    @SerialName("total_harga") val totalHarga: Double,  // Total harga dalam format Decimal(10,2)
    @SerialName("status_pembayaran") val statusPembayaran: StatusPembayaran  // Status pembayaran
)

@Serializable
enum class StatusPembayaran {
    @SerialName("Lunas") LUNAS,
    @SerialName("Belum Lunas") BELUM_LUNAS
}
