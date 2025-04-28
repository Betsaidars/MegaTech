package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.megatech.Components.ProductoItem
import com.example.megatech.Model.ItemsModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDeDeseosView(navController: NavController, listaDeDeseosViewModel: ListaDeDeseosViewModel) {
    val wishlistItems by listaDeDeseosViewModel.wishlistItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Deseos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Log.d("ListaDeDeseosView", "Tamaño de la lista al componer: ${wishlistItems.size}")
            if (wishlistItems.isEmpty()) {
                Text(
                    "Tu lista de deseos está vacía.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(wishlistItems) { item ->
                        Log.d("ListaDeDeseos", "Item en la lista: ${item.name}, ID: ${item.id}")
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            ProductoItem(producto = item, navController = navController)
                            Button(onClick = { listaDeDeseosViewModel.removeItemFromWishlist(item) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}