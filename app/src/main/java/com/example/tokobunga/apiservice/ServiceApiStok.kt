package com.example.tokobunga.apiservice

import com.example.tokobunga.modeldata.LogStok
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiStok {
    @FormUrlEncoded
    @POST("stok_update.php")
    suspend fun updateStok(
        @Field("id_bunga") idBunga: Int,
        @Field("jumlah") jumlah: Int,
        @Field("tipe") tipe: String
    ): Response<Void>

    // PERBAIKAN: Fungsi ini sekarang berada langsung di bawah ServiceApiStok utama
    @GET("log_stok_list.php")
    suspend fun getLogStok(
        @Query("id_bunga") idBunga: Int,
        @Query("bulan") bulan: Int,
        @Query("tahun") tahun: Int
    ): List<LogStok>
}