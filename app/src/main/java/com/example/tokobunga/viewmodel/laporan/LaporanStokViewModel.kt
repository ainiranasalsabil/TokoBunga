package com.example.tokobunga.viewmodel.laporan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokobunga.modeldata.LogStok
import com.example.tokobunga.repositori.RepositoryStok
import kotlinx.coroutines.launch

class LaporanStokViewModel(
    private val repositoryStok: RepositoryStok
) : ViewModel() {

    var listLog: List<LogStok> by mutableStateOf(emptyList())
        private set

    // Status untuk menyimpan bulan yang dipilih (0 = Semua)
    var selectedBulan by mutableStateOf(0)
        private set

    init {
        loadLog(idBunga = 0, bulan = 0)
    }

    // Update fungsi untuk menerima parameter bulan
    fun loadLog(idBunga: Int, bulan: Int) {
        viewModelScope.launch {
            try {
                // Mengambil tahun saat ini secara otomatis untuk filter PHP
                val tahunSekarang = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                listLog = repositoryStok.getLogStok(idBunga, bulan, tahunSekarang)
            } catch (e: Exception) {
                listLog = emptyList()
            }
        }
    }
}

