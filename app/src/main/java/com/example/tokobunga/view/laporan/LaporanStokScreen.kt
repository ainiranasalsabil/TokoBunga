package com.example.tokobunga.view.laporan


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.tokobunga.viewmodel.laporan.LaporanStokViewModel
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanStokScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LaporanStokViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
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

        if (viewModel.listLog.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.loading))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                )
            ) {
                items(viewModel.listLog) { log ->
                    ItemLogStok(log = log)
                }
            }
        }
    }
}

@Composable
fun ItemLogStok(
    log: LogStok,
    modifier: Modifier = Modifier
) {
    val warna = if (log.jenis.lowercase() == "masuk") {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = log.nama_bunga,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Jenis : ${log.jenis}",
                color = warna
            )

            Text(
                text = "Jumlah : ${log.jumlah}"
            )

            Text(
                text = "Tanggal : ${log.tanggal}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
