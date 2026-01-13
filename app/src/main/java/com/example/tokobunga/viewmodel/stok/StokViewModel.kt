package com.example.tokobunga.viewmodel.stok

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.LogStok
import com.example.tokobunga.repositori.RepositoryStok
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// ================= STATUS UI =================
sealed interface StatusStokUi {
    object Idle : StatusStokUi
    object Loading : StatusStokUi
    object Success : StatusStokUi
    data class Error(val message: String) : StatusStokUi
}

class StokViewModel(
    private val repositoryStok: RepositoryStok
) : ViewModel() {

    // ===== INPUT STATE =====
    var jumlah by mutableStateOf("")
        private set

    var jenis by mutableStateOf("MASUK") // MASUK / KELUAR
        private set

    // ===== UI STATE =====
    var statusUi: StatusStokUi by mutableStateOf(StatusStokUi.Idle)
        private set

    // ===== LOG STOK =====
    var logStok by mutableStateOf<List<LogStok>>(emptyList())
        private set

    // ================= INPUT HANDLER =================
    fun onJumlahChange(value: String) {
        if (value.all { it.isDigit() }) {
            jumlah = value
        }
    }

    fun onJenisChange(value: String) {
        jenis = value
    }

    // ================= LOAD LOG =================
    fun loadLogStok(idBunga: Int) {
        viewModelScope.launch {
            try {
                logStok = repositoryStok.getLogStok(idBunga)
            } catch (e: Exception) {
                // log kosong saja, UI tidak crash
                logStok = emptyList()
            }
        }
    }

    // ================= UPDATE STOK =================
    fun submitStok(idBunga: Int) {
        if (jumlah.isBlank()) {
            statusUi = StatusStokUi.Error("Jumlah tidak boleh kosong")
            return
        }

        statusUi = StatusStokUi.Loading

        viewModelScope.launch {
            try {
                val response = repositoryStok.updateStok(
                    idBunga = idBunga.toString(),
                    jenis = jenis,
                    jumlah = jumlah
                )

                if (response.isSuccessful) {
                    statusUi = StatusStokUi.Success
                    jumlah = ""
                    loadLogStok(idBunga)
                } else {
                    statusUi = StatusStokUi.Error("Gagal update stok")
                }

            } catch (e: IOException) {
                statusUi = StatusStokUi.Error("Koneksi gagal")
            } catch (e: HttpException) {
                statusUi = StatusStokUi.Error("Server error")
            }
        }
    }

    fun resetStatus() {
        statusUi = StatusStokUi.Idle
    }
}
