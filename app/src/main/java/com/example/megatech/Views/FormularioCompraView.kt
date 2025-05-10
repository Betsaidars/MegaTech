// FormularioCompraView.kt

package com.example.megatech.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Model.ItemsModel
import com.example.megatech.ViewModels.CarritoDeCompraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCompraView(navController: NavController, carritoDeCompraViewModel: CarritoDeCompraViewModel) {
    val carritoListItems by carritoDeCompraViewModel.itemCarrito.collectAsState()
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    val totalPrecioSeleccionado by carritoDeCompraViewModel.totalPrecioSeleccionado.collectAsState(initial = 0.0) // Recogemos el total


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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total: ${String.format("%.2f", totalPrecioSeleccionado)}€",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = {
                        navController.navigate("compraRealizada") {
                            popUpTo("carritoDeCompra") { inclusive = true }
                        }
                    },
                    enabled = nombre.isNotBlank() && direccion.isNotBlank() && carritoListItems.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pagar")
                }
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
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(carritoListItems) { item ->
                    FormularioItemRow(item = item) // Usamos una nueva composable para cada item
                    Divider() // Añade una línea divisoria entre items
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
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
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}

@Composable
fun FormularioItemRow(item: ItemsModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Imagen del producto
        item.imageUrl.firstOrNull()?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 8.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = item.name ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(text = "$${item.price ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        // Aquí podrías añadir la cantidad si la tienes en tu modelo de carrito
    }
}