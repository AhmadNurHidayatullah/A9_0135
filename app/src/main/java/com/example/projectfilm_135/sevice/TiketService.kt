package com.example.projectfilm_135.sevice


import com.example.projectfilm_135.model.Tiket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TiketService {
    @GET("bacatiket.php")
    suspend fun getTikets(): List<Tiket>  // Mengambil semua data tiket

    @GET("baca1tiket.php")
    suspend fun getTiketById(@Query("id_tiket") idTiket: Int): Tiket  // Mengambil tiket berdasarkan ID

    @POST("tambahtiket.php")
    suspend fun insertTiket(@Body tiket: Tiket)  // Menambahkan data tiket baru

    @PUT("updatetiket.php/{id_tiket}")
    suspend fun updateTiket(@Path("id_tiket") idTiket: Int, @Body tiket: Tiket)  // Mengupdate data tiket berdasarkan ID

    @DELETE("deletetiket.php/{id_tiket}")
    suspend fun deleteTiket(@Path("id_tiket") idTiket: Int): Response<Void>  // Menghapus data tiket berdasarkan ID
}
