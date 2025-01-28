package com.example.projectfilm_135.repository


import com.example.projectfilm_135.model.Tiket
import com.example.projectfilm_135.sevice.TiketService
import java.io.IOException

interface TiketRepository {
    suspend fun getTikets(): List<Tiket>
    suspend fun insertTiket(tiket: Tiket)
    suspend fun updateTiket(idTiket: Int, tiket: Tiket)
    suspend fun deleteTiket(idTiket: Int)
    suspend fun getTiketById(idTiket: Int): Tiket
}

class NetworkTiketRepository(
    private val tiketAPIService: TiketService
) : TiketRepository {

    override suspend fun insertTiket(tiket: Tiket) {
        tiketAPIService.insertTiket(tiket)
    }

    override suspend fun updateTiket(idTiket: Int, tiket: Tiket) {
        tiketAPIService.updateTiket(idTiket, tiket)
    }

    override suspend fun deleteTiket(idTiket: Int) {
        try {
            val response = tiketAPIService.deleteTiket(idTiket)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete tiket. HTTP Status code: ${response.code()}")
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getTikets(): List<Tiket> = tiketAPIService.getTikets()

    override suspend fun getTiketById(idTiket: Int): Tiket {
        val response = tiketAPIService.getTiketById(idTiket)
        if (response != null) {
            return response
        } else {
            throw Exception("Data tiket dengan id $idTiket tidak ditemukan.")
        }
    }
}
