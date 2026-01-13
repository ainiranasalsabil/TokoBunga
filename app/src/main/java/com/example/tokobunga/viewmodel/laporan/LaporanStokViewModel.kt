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

    fun loadLog(idBunga: Int) {
        viewModelScope.launch {
            listLog = repositoryStok.getLogStok(idBunga)
        }
    }
}
