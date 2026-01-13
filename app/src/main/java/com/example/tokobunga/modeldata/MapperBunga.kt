package com.example.tokobunga.modeldata

fun DetailBunga.toBunga(namaFileFoto: String?): Bunga =
    Bunga(
        id_bunga = id_bunga,
        nama_bunga = nama_bunga,
        kategori = kategori,
        harga = harga,
        stok = stok.toInt(),
        foto_bunga = namaFileFoto
    )

fun Bunga.toDetailBunga(): DetailBunga =
    DetailBunga(
        id_bunga = id_bunga,
        nama_bunga = nama_bunga,
        kategori = kategori,
        harga = harga.toString(),
        stok = stok.toString(),
        fotoUri = null // karena dari server
    )

fun Bunga.toUIStateBunga(isEntryValid: Boolean = false): UIStateBunga =
    UIStateBunga(
        detailBunga = this.toDetailBunga(),
        isEntryValid = isEntryValid
    )
