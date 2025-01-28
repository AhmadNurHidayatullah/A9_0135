package com.example.projectfilm_135.repository


import com.example.projectfilm_135.model.Penayangan
import com.example.projectfilm_135.sevice.PenayanganService
import java.io.IOException


interface PenayanganRepository {
    suspend fun getPenayangan(): List<Penayangan>
    suspend fun insertPenayangan(penayangan: Penayangan)
    suspend fun updatePenayangan(idPenayangan: Int, penayangan: Penayangan)
    suspend fun deletePenayangan(idPenayangan: Int)
    suspend fun getPenayanganById(idPenayangan: Int): Penayangan
}

class NetworkPenayanganRepository(
    private val penayanganAPIService: PenayanganService
) : PenayanganRepository {

    override suspend fun insertPenayangan(penayangan: Penayangan) {
        penayanganAPIService.insertPenayangan(penayangan)
    }

    override suspend fun updatePenayangan(idPenayangan: Int, penayangan: Penayangan) {
        penayanganAPIService.updatePenayangan(idPenayangan, penayangan)
    }

    override suspend fun deletePenayangan(idPenayangan: Int) {
        try {
            val response = penayanganAPIService.deletePenayangan(idPenayangan)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete penayangan. HTTP Status code: ${response.code()}")
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPenayangan(): List<Penayangan> = penayanganAPIService.getPenayangans()

    override suspend fun getPenayanganById(idPenayangan: Int): Penayangan {
        val response = penayanganAPIService.getPenayanganById(idPenayangan)
        if (response != null) {
            return response
        } else {
            throw Exception("Data penayangan dengan id $idPenayangan tidak ditemukan.")
        }
    }

}
