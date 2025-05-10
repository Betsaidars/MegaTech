// FormularioCompraView.kt

package com.example.megatech.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.megatech.ViewModels.CarritoDeCompraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCompraView(navController: NavController, carritoDeCompraViewModel: CarritoDeCompraViewModel) {
    val carritoListItems by carritoDeCompraViewModel.itemCarrito.collectAsState()
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Información de Envío") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver al carrito")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    navController.navigate("compraRealizada") {
                        popUpTo("carritoDeCompra") { inclusive = true } // Opcional: eliminar carrito del back stack
                    }
                },
                enabled = nombre.isNotBlank() && direccion.isNotBlank() && carritoListItems.isNotEmpty()
            ) {
                Text("Pagar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Artículos en tu carrito:", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(carritoListItems) { item ->
                    Text(item.name ?: "") // Muestra solo el nombre por ahora
                    // Podrías reutilizar CartItemRow pero sin los botones de eliminar/añadir a lista de deseos
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}