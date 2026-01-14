package com.example.tokobunga.viewmodel.stok

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.LogStok
import com.example.tokobunga.repositori.RepositoryStok
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar // Penting untuk mendapatkan tahun otomatis

sealed interface StatusStokUi {
    object Idle : StatusStokUi
    object Loading : StatusStokUi
    object Success : StatusStokUi
    data class Error(val message: String) : StatusStokUi
}

class StokViewModel(
    private val repositoryStok: RepositoryStok
) : ViewModel() {

    var jumlah by mutableStateOf("")
        private set

    var jenis by mutableStateOf("Masuk")
        private set

    var statusUi: StatusStokUi by mutableStateOf(StatusStokUi.Idle)
        private set

    var logStok by mutableStateOf<List<LogStok>>(emptyList())
        private set

    fun onJumlahChange(value: String) {
        if (value.all { it.isDigit() }) {
            jumlah = value
        }
    }

    fun onJenisChange(value: String) {
        jenis = value.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }

    /**
     * PERBAIKAN: Menambahkan parameter Tahun untuk sinkronisasi Repository.
     * Mengirim bulan = 0 dan tahun saat ini agar menampilkan semua riwayat tahun ini.
     */
    fun loadLogStok(idBunga: Int) {
        viewModelScope.launch {
            try {
                // Ambil tahun saat ini secara dinamis
                val tahunSekarang = Calendar.getInstance().get(Calendar.YEAR)

                // PERBAIKAN UTAMA: Sekarang mengirimkan 3 argumen (id, bulan, tahun)
                logStok = repositoryStok.getLogStok(
                    idBunga = idBunga,
                    bulan = 0,
                    tahun = tahunSekarang
                )
            } catch (e: Exception) {
                logStok = emptyList()
            }
        }
    }

    fun submitStok(idBunga: Int) {
        if (jumlah.isBlank() || jumlah.toInt() <= 0) {
            statusUi = StatusStokUi.Error("Jumlah harus lebih dari 0")
            return
        }

        statusUi = StatusStokUi.Loading

        viewModelScope.launch {
            try {
                val response = repositoryStok.updateStok(
                    idBunga = idBunga,
                    tipe = jenis,
                    jumlah = jumlah.toInt()
                )

                if (response.isSuccessful) {
                    statusUi = StatusStokUi.Success
                    jumlah = ""
                    loadLogStok(idBunga) // Refresh log setelah update
                } else {
                    val errorJson = response.errorBody()?.string()
                    val message = try {
                        JSONObject(errorJson.orEmpty()).getString("error")
                    } catch (e: Exception) {
                        "Gagal memperbarui stok"
                    }
                    statusUi = StatusStokUi.Error(message)
                }

            } catch (e: IOException) {
                statusUi = StatusStokUi.Error("Masalah koneksi internet")
            } catch (e: Exception) {
                statusUi = StatusStokUi.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun resetStatus() {
        statusUi = StatusStokUi.Idle
    }
}