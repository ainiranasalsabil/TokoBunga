package com.example.tokobunga.view.bunga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tokobunga.R
import com.example.tokobunga.view.components.DropdownKategori
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.bunga.EntryBungaViewModel
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBungaScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryBungaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // ⬇️ SIMPAN FILE FOTO (sementara)
    var fotoFile by remember { mutableStateOf<File?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FloristTopAppBar(
                title = stringResource(R.string.entry_bunga),
                canNavigateBack = true,
                onNavigateBack = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            )
        ) {

            OutlinedTextField(
                value = viewModel.nama,
                onValueChange = viewModel::onNamaChange,
                label = { Text(stringResource(R.string.nama_bunga)) },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownKategori(
                selected = viewModel.kategori,
                onSelected = viewModel::onKategoriChange
            )


            OutlinedTextField(
                value = viewModel.harga,
                onValueChange = viewModel::onHargaChange,
                label = { Text(stringResource(R.string.harga)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.stok,
                onValueChange = viewModel::onStokChange,
                label = { Text(stringResource(R.string.stok)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // ⚠️ sementara: anggap foto SUDAH ADA
            Button(
                onClick = {
                    if (fotoFile == null) return@Button

                    viewModel.submitBunga(
                        fileFoto = fotoFile!!,
                        onSuccess = navigateBack,
                        onError = { /* tampilkan snackbar */ }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_simpan))
            }
        }
    }
}
