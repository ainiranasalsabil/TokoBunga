package com.example.tokobunga.apiservice

import com.example.tokobunga.modeldata.Bunga
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiBunga {

    @GET("bunga_list.php")
    suspend fun getListBunga(): List<Bunga>

    @GET("bunga_detail.php")
    suspend fun getDetailBunga(
        @Query("id") idBunga: Int
    ): Bunga

    @Multipart
    @POST("bunga_add.php")
    suspend fun tambahBunga(
        @Part("nama_bunga") nama: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part foto: MultipartBody.Part
    ): Response<Void>

    @Multipart
    @POST("bunga_update.php")
    suspend fun updateBunga(
        @Query("id") idBunga: Int,
        @Part("nama_bunga") nama: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part foto: MultipartBody.Part?
    ): Response<Void>

    @DELETE("bunga_delete.php")
    suspend fun deleteBunga(
        @Query("id") idBunga: Int
    ): Response<Void>
}
