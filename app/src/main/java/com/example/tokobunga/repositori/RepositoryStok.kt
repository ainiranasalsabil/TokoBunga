package com.example.tokobunga.repositori

import com.example.tokobunga.apiservice.ServiceApiStok
import com.example.tokobunga.modeldata.LogStok
import retrofit2.Response

interface RepositoryStok {
    suspend fun updateStok(
        idBunga: Int,
        tipe: String,
        jumlah: Int
    ): Response<Void>

    // Mendukung filter Bunga, Bulan, dan Tahun agar sinkron dengan PHP
    suspend fun getLogStok(idBunga: Int, bulan: Int, tahun: Int): List<LogStok>
}

class JaringanRepositoryStok(
    private val serviceApiStok: ServiceApiStok
) : RepositoryStok {

    override suspend fun updateStok(
        idBunga: Int,
        tipe: String,
        jumlah: Int
    ): Response<Void> {
        return serviceApiStok.updateStok(idBunga, jumlah, tipe)
    }

    override suspend fun getLogStok(idBunga: Int, bulan: Int, tahun: Int): List<LogStok> {
        // Sekarang memanggil fungsi getLogStok dengan 3 parameter
        return serviceApiStok.getLogStok(idBunga, bulan, tahun)
    }
}