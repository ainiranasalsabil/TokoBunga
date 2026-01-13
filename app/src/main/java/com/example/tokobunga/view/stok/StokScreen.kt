package com.example.tokobunga.view.stok

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tokobunga.R
import com.example.tokobunga.modeldata.LogStok
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.stok.StatusStokUi
import com.example.tokobunga.viewmodel.stok.StokViewModel
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StokScreen(
    idBunga: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StokViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

    // load log pertama kali
    LaunchedEffect(idBunga) {
        viewModel.loadLogStok(idBunga)
    }

    Scaffold(
        topBar = {
            FloristTopAppBar(
                title = stringResource(R.string.stok_title),
                canNavigateBack = true,
                onNavigateBack = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_medium)
            )
        ) {

            // ================= FORM INPUT =================
            OutlinedTextField(
                value = viewModel.jumlah,
                onValueChange = viewModel::onJumlahChange,
                label = { Text(stringResource(R.string.stok)) },
                modifier = Modifier.fillMaxWidth()
            )

            JenisStokDropdown(
                selected = viewModel.jenis,
                onSelected = viewModel::onJenisChange
            )

            Button(
                onClick = { viewModel.submitStok(idBunga) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_simpan))
            }

            // ================= STATUS =================
            when (val status = viewModel.statusUi) {
                is StatusStokUi.Loading -> {
                    CircularProgressIndicator()
                }

                is StatusStokUi.Error -> {
                    Text(
                        text = status.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is StatusStokUi.Success -> {
                    Text(
                        text = "Stok berhasil diperbarui",
                        color = MaterialTheme.colorScheme.primary
                    )
                    viewModel.resetStatus()
                }

                else -> {}
            }

            Divider()

            // ================= LOG STOK =================
            Text(
                text = "Riwayat Stok",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                )
            ) {
                items(viewModel.logStok) { log ->
                    ItemLogStok(log)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JenisStokDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    val options = listOf("MASUK", "KELUAR")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Jenis Stok") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ItemLogStok(log: LogStok) {

    val warna = if (log.jenis.uppercase() == "MASUK")
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_medium)
            )
        ) {
            Text(
                text = log.nama_bunga,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Jenis : ${log.jenis}",
                color = warna
            )
            Text("Jumlah : ${log.jumlah}")
            Text(
                text = log.tanggal,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
