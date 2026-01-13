package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiBunga
import com.example.tokobunga.modeldata.Bunga
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface RepositoryBunga {

    suspend fun getListBunga(): List<Bunga>
    suspend fun getDetailBunga(id: Int): Bunga

    suspend fun tambahBunga(
        nama: RequestBody,
        kategori: RequestBody,
        harga: RequestBody,
        stok: RequestBody,
        foto: MultipartBody.Part
    ): Response<Void>

    suspend fun updateBunga(
        id: Int,
        nama: RequestBody,
        kategori: RequestBody,
        harga: RequestBody,
        stok: RequestBody,
        foto: MultipartBody.Part?
    ): Response<Void>

    suspend fun deleteBunga(id: Int): Response<Void>
}

class JaringanRepositoryBunga(
    private val serviceApiBunga: ServiceApiBunga
) : RepositoryBunga {

    override suspend fun getListBunga(): List<Bunga> =
        serviceApiBunga.getListBunga()

    override suspend fun getDetailBunga(id: Int): Bunga =
        serviceApiBunga.getDetailBunga(id)

    override suspend fun tambahBunga(
        nama: RequestBody,
        kategori: RequestBody,
        harga: RequestBody,
        stok: RequestBody,
        foto: MultipartBody.Part
    ): Response<Void> =
        serviceApiBunga.tambahBunga(nama, kategori, harga, stok, foto)

    override suspend fun updateBunga(
        id: Int,
        nama: RequestBody,
        kategori: RequestBody,
        harga: RequestBody,
        stok: RequestBody,
        foto: MultipartBody.Part?
    ): Response<Void> =
        serviceApiBunga.updateBunga(id, nama, kategori, harga, stok, foto)

    override suspend fun deleteBunga(id: Int): Response<Void> =
        serviceApiBunga.deleteBunga(id)
}
