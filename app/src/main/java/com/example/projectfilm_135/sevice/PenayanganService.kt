package com.example.projectfilm_135.sevice

import com.example.projectfilm_135.model.Penayangan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PenayanganService {
    @GET("bacapenayangan.php")
    suspend fun getPenayangans(): List<Penayangan>

    @GET("baca1penayangan.php")
    suspend fun getPenayanganById(@Query("id_penayangan") idPenayangan: Int): Penayangan


    @POST("tambahpenayangan.php")
    suspend fun insertPenayangan(@Body penayangan: Penayangan)

    @PUT("updatepenayangan.php/{id_penayangan}")
    suspend fun updatePenayangan(@Path("id_penayangan") idPenayangan: Int, @Body penayangan: Penayangan)

    @DELETE("deletepenayangan.php/{id_penayangan}")
    suspend fun deletePenayangan(@Path("id_penayangan") idPenayangan: Int): Response<Void>
}
