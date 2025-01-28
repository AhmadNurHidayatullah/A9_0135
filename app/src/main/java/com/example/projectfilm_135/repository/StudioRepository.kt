package com.example.projectfilm_135.repository

import com.example.projectfilm_135.model.Studio
import com.example.projectfilm_135.sevice.StudioService
import java.io.IOException

interface StudioRepository {
    suspend fun getStudios(): List<Studio>
    suspend fun insertStudio(studio: Studio)
    suspend fun updateStudio(idStudio: Int, studio: Studio)
    suspend fun deleteStudio(idStudio: Int)
    suspend fun getStudioById(idStudio: Int): Studio
}

class NetworkStudioRepository(
    private val studioAPIService: StudioService
) : StudioRepository {

    override suspend fun insertStudio(studio: Studio) {
        studioAPIService.insertStudio(studio)
    }

    override suspend fun updateStudio(idStudio: Int, studio: Studio) {
        studioAPIService.updateStudio(idStudio, studio)
    }

    override suspend fun deleteStudio(idStudio: Int) {
        try {
            val response = studioAPIService.deleteStudio(idStudio)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete studio. HTTP Status code: ${response.code()}")
            } else {
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getStudios(): List<Studio> = studioAPIService.getStudios()

    override suspend fun getStudioById(idStudio: Int): Studio {
        return studioAPIService.getStudioById(idStudio)
    }
}
