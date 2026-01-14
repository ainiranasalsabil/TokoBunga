package com.example.tokobunga.viewmodel.bunga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.repositori.RepositoryBunga
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EntryBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    var nama by mutableStateOf("")
        private set
    var kategori by mutableStateOf("")
        private set
    var harga by mutableStateOf("")
        private set

    // State stok dihapus karena akan otomatis diset "0" saat submit

    fun onNamaChange(value: String) { nama = value }
    fun onKategoriChange(value: String) { kategori = value }
    fun onHargaChange(value: String) { harga = value }

    fun submitBunga(
        fileFoto: File,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nama.isBlank() || kategori.isBlank() || harga.isBlank()) {
            onError("Nama, Kategori, dan Harga wajib diisi!")
            return
        }

        viewModelScope.launch {
            try {
                // GUNAKAN toMediaType() karena sudah di-import di atas
                val requestFile = fileFoto.asRequestBody("image/jpeg".toMediaType())

                val fotoPart = MultipartBody.Part.createFormData(
                    "foto",
                    fileFoto.name,
                    requestFile
                )

                val response = repositoryBunga.tambahBunga(
                    nama = nama,
                    kategori = kategori,
                    harga = harga,
                    stok = "0",
                    foto = fotoPart
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Gagal simpan: Server merespon ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Koneksi Error: ${e.localizedMessage}")
            }
        }
    }
}