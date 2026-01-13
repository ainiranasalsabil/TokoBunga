package com.example.tokobunga.apiservice

import com.example.tokobunga.modeldata.LogStok
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiStok {

    @POST("stok_update.php")
    suspend fun updateStok(
        @Body request: Map<String, String>
    ): Response<Void>

    @GET("log_stok_list.php")
    suspend fun getLogStok(
        @Query("id_bunga") idBunga: Int
    ): List<LogStok>
}
