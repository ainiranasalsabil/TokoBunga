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
import org.json.JSONObject
import java.io.File

class EditBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    var idBunga: Int = 0
        private set

    var nama by mutableStateOf("")
        private set

    var kategori by mutableStateOf("")
        private set

    var harga by mutableStateOf("")
        private set

    /** 🔒 data terkunci jika sudah ada log stok */
    var isLocked by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var fotoFile: File? = null

    fun clearError() {
        errorMessage = null
    }

    // ================= LOAD DATA =================
    fun loadDataById(id: Int) {
        viewModelScope.launch {
            try {
                val bunga = repositoryBunga.getDetailBunga(id)
                loadBunga(bunga)
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data bunga"
            }
        }
    }

    private fun loadBunga(bunga: Bunga) {
        idBunga = bunga.id_bunga
        nama = bunga.nama_bunga
        kategori = bunga.kategori
        harga = bunga.harga

        /**
         * NOTE:
         * kalau backend detail bunga sudah kirim can_edit,
         * tinggal aktifkan ini:
         *
         * isLocked = !bunga.can_edit
         */
    }

    // ================= INPUT =================
    fun onNamaChange(value: String) {
        if (!isLocked) nama = value
    }

    fun onKategoriChange(value: String) {
        if (!isLocked) kategori = value
    }

    fun onHargaChange(value: String) {
        if (!isLocked) harga = value
    }

    fun onFotoSelected(file: File) {
        fotoFile = file
    }

    // ================= UPDATE =================
    fun updateBunga(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (nama.isBlank() || kategori.isBlank() || harga.isBlank()) {
            onError("Nama, kategori, dan harga wajib diisi")
            return
        }

        viewModelScope.launch {
            try {
                val fotoPart = fotoFile?.let {
                    MultipartBody.Part.createFormData(
                        "foto",
                        it.name,
                        it.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                val response = repositoryBunga.updateBunga(
                    id = idBunga,
                    nama = nama,
                    kategori = kategori,
                    harga = harga,
                    foto = fotoPart
                )

                if (response.isSuccessful) {
                    fotoFile = null // ✅ reset
                    onSuccess()
                } else {
                    val pesan = when (response.code()) {
                        409 -> {
                            isLocked = true
                            "Data bunga tidak dapat diperbarui karena sudah memiliki riwayat stok"
                        }
                        else -> {
                            val raw = response.errorBody()?.string()
                            try {
                                JSONObject(raw.orEmpty()).getString("error")
                            } catch (e: Exception) {
                                "Gagal memperbarui data bunga"
                            }
                        }
                    }

                    errorMessage = pesan
                    onError(pesan)
                }
            } catch (e: Exception) {
                val msg = "Koneksi ke server bermasalah"
                errorMessage = msg
                onError(msg)
            }
        }
    }
}

