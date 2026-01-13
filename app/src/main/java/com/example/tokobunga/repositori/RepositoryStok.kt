package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiStok
import com.example.tokobunga.modeldata.LogStok
import retrofit2.Response

interface RepositoryStok {

    suspend fun updateStok(
        idBunga: String,
        jenis: String,
        jumlah: String
    ): Response<Void>

    suspend fun getLogStok(idBunga: Int): List<LogStok>
}

class JaringanRepositoryStok(
    private val serviceApiStok: ServiceApiStok
) : RepositoryStok {

    override suspend fun updateStok(
        idBunga: String,
        jenis: String,
        jumlah: String
    ): Response<Void> {
        val body = mapOf(
            "id_bunga" to idBunga,
            "jenis" to jenis,
            "jumlah" to jumlah
        )
        return serviceApiStok.updateStok(body)
    }

    override suspend fun getLogStok(idBunga: Int): List<LogStok> =
        serviceApiStok.getLogStok(idBunga)
}

