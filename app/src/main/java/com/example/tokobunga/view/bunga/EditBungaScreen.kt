package com.example.tokobunga.view.bunga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tokobunga.R
import com.example.tokobunga.view.components.DropdownKategori
import com.example.tokobunga.view.components.FloristTopAppBar
import com.example.tokobunga.viewmodel.bunga.EditBungaViewModel
import com.example.tokobunga.viewmodel.provide.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBungaScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditBungaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FloristTopAppBar(
                title = stringResource(R.string.edit_bunga),
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

            Text(
                text = stringResource(R.string.required_field),
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_small)
                )
            )

            Button(
                onClick = {
                    viewModel.updateBunga(
                        onSuccess = {
                            navigateBack()
                        },
                        onError = { errorMessage ->
                            // sementara bisa log / nanti snackbar
                            println(errorMessage)
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_update))
            }
        }
    }
}
