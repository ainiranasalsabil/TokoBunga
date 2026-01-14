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

    // PERBAIKAN: Stok kita hilangkan dari state pengeditan
    // agar admin tidak bisa edit angka stok di layar ini.

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun clearError() { errorMessage = null }

    private var fotoFile: File? = null

    fun loadDataById(id: Int) {
        viewModelScope.launch {
            try {
                // Pastikan repository menggunakan id_bunga
                val bunga = repositoryBunga.getDetailBunga(id)
                loadBunga(bunga)
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data: ${e.message}"
            }
        }
    }

    private fun loadBunga(bunga: Bunga) {
        idBunga = bunga.id_bunga
        nama = bunga.nama_bunga
        kategori = bunga.kategori
        harga = bunga.harga
        // stok tidak di-load ke field input
    }

    fun onNamaChange(value: String) { nama = value }
    fun onKategoriChange(value: String) { kategori = value }
    fun onHargaChange(value: String) { harga = value }
    fun onFotoSelected(file: File) { fotoFile = file }

    fun updateBunga(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Validasi: Stok tidak perlu divalidasi lagi di sini
        if (nama.isBlank() || kategori.isBlank() || harga.isBlank()) {
            onError("Nama, Kategori, dan Harga wajib diisi!")
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

                /**
                 * PERBAIKAN:
                 * Stok dikirim sebagai String kosong atau nilai tertentu.
                 * Di PHP (bunga_update.php), kolom 'stok' harus dihilangkan dari query UPDATE
                 * agar angka stok yang sudah ada di database tidak berubah/tertimpa.
                 */
                val response = repositoryBunga.updateBunga(
                    id = idBunga,
                    nama = nama,
                    kategori = kategori,
                    harga = harga,
                    foto = fotoPart
                )

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorRaw = response.errorBody()?.string()
                    val pesan = try {
                        JSONObject(errorRaw.orEmpty()).getString("error")
                    } catch (e: Exception) {
                        "Gagal memperbarui data"
                    }
                    errorMessage = pesan
                    onError(pesan)
                }
            } catch (e: Exception) {
                val msg = e.message ?: "Gagal memperbarui data bunga"
                errorMessage = msg
                onError(msg)
            }
        }
    }
}