package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiBunga
import com.example.tokobunga.modeldata.Bunga
import okhttp3.MultipartBody
import retrofit2.Response

interface RepositoryBunga {
    suspend fun getListBunga(): List<Bunga>

    // PERBAIKAN: Nama parameter tetap 'id' di level Repo tidak apa-apa,
    // tapi pastikan dilempar ke id_bunga di ServiceApi
    suspend fun getDetailBunga(id: Int): Bunga

    suspend fun tambahBunga(
        nama: String,
        kategori: String,
        harga: String,
        stok: String,
        foto: MultipartBody.Part
    ): Response<Void>

    suspend fun updateBunga(
        id: Int,
        nama: String,
        kategori: String,
        harga: String,
        foto: MultipartBody.Part?
    ): Response<Void>


    suspend fun deleteBunga(id: Int): Response<Void>
}

class JaringanRepositoryBunga(
    private val serviceApiBunga: ServiceApiBunga
) : RepositoryBunga {

    override suspend fun getListBunga(): List<Bunga> =
        serviceApiBunga.getListBunga()

    // SINKRONISASI: Mengarahkan id ke parameter id_bunga di ServiceApi
    override suspend fun getDetailBunga(id: Int): Bunga =
        serviceApiBunga.getDetailBunga(idBunga = id)

    override suspend fun tambahBunga(
        nama: String,
        kategori: String,
        harga: String,
        stok: String,
        foto: MultipartBody.Part
    ): Response<Void> =
        serviceApiBunga.tambahBunga(nama, kategori, harga, stok, foto)

    // SINKRONISASI: Mengarahkan id ke id_bunga dan menghapus parameter stok yang tidak perlu
    override suspend fun updateBunga(
        id: Int,
        nama: String,
        kategori: String,
        harga: String,
        foto: MultipartBody.Part?
    ): Response<Void> =
        serviceApiBunga.updateBunga(
            idBunga = id,
            nama = nama,
            kategori = kategori,
            harga = harga,
            foto = foto
        )

    // SINKRONISASI: Memastikan ID dikirim ke id_bunga
    override suspend fun deleteBunga(id: Int): Response<Void> =
        serviceApiBunga.deleteBunga(idBunga = id)
}