package com.example.tokobunga.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Bunga(
    val id_bunga: Int,
    val nama_bunga: String,
    val kategori: String,
    val harga: String,   // ✅ VARCHAR
    val stok: Int,       // ✅ INT
    val foto_bunga: String?
)

