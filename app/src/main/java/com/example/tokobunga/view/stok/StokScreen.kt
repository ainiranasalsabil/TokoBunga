package com.example.tokobunga.view.stok

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tokobunga.R
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel
import com.example.tokobunga.viewmodel.stok.StatusStokUi
import com.example.tokobunga.viewmodel.stok.StokViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StokScreen(
    idBunga: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StokViewModel = viewModel(factory = PenyediaViewModel.Factory)
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

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ================= FORM STOK =================
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Jumlah Stok
                    OutlinedTextField(
                        value = viewModel.jumlah,
                        onValueChange = viewModel::onJumlahChange,
                        label = { Text(stringResource(R.string.stok)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Jenis Stok
                    JenisStokDropdown(
                        selected = viewModel.jenis,
                        onSelected = viewModel::onJenisChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ================= SIMPAN =================
            Button(
                onClick = { viewModel.submitStok(idBunga) },
                enabled = viewModel.statusUi !is StatusStokUi.Loading,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(160.dp)
                    .height(46.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(stringResource(R.string.btn_simpan))
            }

            // ================= STATUS =================
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (val status = viewModel.statusUi) {

                    is StatusStokUi.Loading -> {
                        CircularProgressIndicator()
                    }

                    is StatusStokUi.Error -> {
                        Text(
                            text = status.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    is StatusStokUi.Success -> {
                        LaunchedEffect(Unit) {
                            delay(1500)
                            viewModel.resetStatus()
                            navigateBack()
                        }
                        Text(
                            text = "Stok berhasil diperbarui!",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    else -> {}
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
    val options = listOf("Masuk", "Keluar")
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
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
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
