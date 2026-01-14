package com.example.tokobunga.viewmodel.bunga

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.repositori.RepositoryBunga
import kotlinx.coroutines.launch
import org.json.JSONObject

class DetailBungaViewModel(
    private val repositoryBunga: RepositoryBunga
) : ViewModel() {

    var statusDetail: StatusDetailBunga by mutableStateOf(StatusDetailBunga.Loading)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        errorMessage = null
    }

    fun loadDetail(id: Int) {
        statusDetail = StatusDetailBunga.Loading
        viewModelScope.launch {
            try {
                val bunga = repositoryBunga.getDetailBunga(id)

                // TIPS: Jika API getDetailBunga milikmu mengirimkan info apakah sudah ada stok_log,
                // kamu bisa mengatur canEdit/canDelete di sini.
                // Untuk sementara, kita set true agar tombol tetap muncul.
                statusDetail = StatusDetailBunga.Success(
                    bunga = bunga,
                    canEdit = true,
                    canDelete = true
                )
            } catch (e: Exception) {
                statusDetail = StatusDetailBunga.Error
            }
        }
    }

    fun hapusBunga(onSuccess: () -> Unit) {
        val current = statusDetail
        if (current is StatusDetailBunga.Success) {
            viewModelScope.launch {
                try {
                    // Pastikan repository mengirim parameter ?id= (Query)
                    val response = repositoryBunga.deleteBunga(current.bunga.id_bunga)

                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        // Ambil error body dan parsing JSON (Sama seperti EditViewModel)
                        val errorRaw = response.errorBody()?.string()
                        val pesan = try {
                            JSONObject(errorRaw ?: "").getString("error")
                        } catch (e: Exception) {
                            "Gagal menghapus: Terdeteksi riwayat stok"
                        }
                        errorMessage = pesan
                    }
                } catch (e: Exception) {
                    errorMessage = "Koneksi ke server bermasalah"
                }
            }
        }
    }
}