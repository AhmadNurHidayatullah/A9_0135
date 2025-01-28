package com.example.projectfilm_135.sevice

import com.example.projectfilm_135.model.Studio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface StudioService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacastudio.php")
    suspend fun getStudios(): List<Studio>

    @GET("baca1studio.php/{id_studio}")
    suspend fun getStudioById(@Query("id_studio") idStudio: Int): Studio

    @POST("tambahstudio.php")
    suspend fun insertStudio(@Body studio: Studio)

    @PUT("updatestudio.php/{id_studio}")
    suspend fun updateStudio(@Query("id_studio") idStudio: Int, @Body studio: Studio)

    @DELETE("deletestudio.php/{id_studio}")
    suspend fun deleteStudio(@Query("id_studio") idStudio: Int): Response<Void>
}