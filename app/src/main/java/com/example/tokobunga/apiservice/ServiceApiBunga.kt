package com.example.tokobunga.apiservice

import com.example.tokobunga.modeldata.Bunga
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiBunga {

    @GET("bunga_list.php")
    suspend fun getListBunga(): List<Bunga>

    @GET("bunga_detail.php")
    suspend fun getDetailBunga(
        @Query("id_bunga") idBunga: Int
    ): Bunga

    @Multipart
    @POST("bunga_add.php")
    suspend fun tambahBunga(
        @Part("nama_bunga") nama: String,
        @Part("kategori") kategori: String,
        @Part("harga") harga: String,
        @Part("stok") stok: String,
        @Part foto: MultipartBody.Part
    ): Response<Void>

    @Multipart
    @POST("bunga_update.php")
    suspend fun updateBunga(
        @Query("id_bunga") idBunga: Int,
        @Part("nama_bunga") nama: String,
        @Part("kategori") kategori: String,
        @Part("harga") harga: String,
        @Part foto: MultipartBody.Part?
    ): Response<Void>

    // 🔥 INI YANG DIPERBAIKI
    @DELETE("bunga_delete.php")
    suspend fun deleteBunga(
        @Query("id_bunga") idBunga: Int
    ): Response<Void>
}
