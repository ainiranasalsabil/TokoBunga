package com.example.tokobunga.viewmodel.bunga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.Bunga
import com.example.tokobunga.repositori.RepositoryBunga
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    // ================= STATE =================
    var idBunga: Int = 0
        private set

    var nama by mutableStateOf("")
        private set

    var kategori by mutableStateOf("")
        private set

    var harga by mutableStateOf("")
        private set

    var stok by mutableStateOf("")
        private set

    private var fotoFile: File? = null

    // ================= LOAD DETAIL =================
    fun loadBunga(bunga: Bunga) {
        idBunga = bunga.id_bunga
        nama = bunga.nama_bunga
        kategori = bunga.kategori
        harga = bunga.harga
        stok = bunga.stok.toString()   // ✅ FIX
    }

    // ================= HANDLER =================
    fun onNamaChange(value: String) { nama = value }
    fun onKategoriChange(value: String) { kategori = value }
    fun onHargaChange(value: String) { harga = value }
    fun onStokChange(value: String) { stok = value }
    fun onFotoSelected(file: File) { fotoFile = file }

    // ================= SUBMIT =================
    fun updateBunga(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nama.isBlank() || kategori.isBlank() || harga.isBlank()) {
            onError("Data tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            try {
                val namaRB = nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val kategoriRB = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
                val hargaRB = harga.toRequestBody("text/plain".toMediaTypeOrNull())
                val stokRB = stok.toRequestBody("text/plain".toMediaTypeOrNull())

                val fotoPart = fotoFile?.let {
                    MultipartBody.Part.createFormData(
                        "foto",
                        it.name,
                        it.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                repositoryBunga.updateBunga(
                    idBunga,
                    namaRB,
                    kategoriRB,
                    hargaRB,
                    stokRB,
                    fotoPart
                )

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal update bunga")
            }
        }
    }
}
