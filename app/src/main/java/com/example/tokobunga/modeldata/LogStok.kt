package com.example.tokobunga.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class LogStok(
    val id_log: Int,
    val id_bunga: Int,
    val nama_bunga: String,
    val jumlah: Int,
    val tipe: String, // Sesuaikan dengan database
    val tanggal: String
)