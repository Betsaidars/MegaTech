package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Components.ProductoItem
import com.example.megatech.Model.ItemsModel
import com.example.megatech.R
import com.example.megatech.ViewModels.CarritoDeCompraViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoDeCompraView(navController: NavController, carritoDeCompraViewModel: CarritoDeCompraViewModel, listaDeDeseosViewModel: ListaDeDeseosViewModel,  onComprarClicked: () -> Unit){

    val carritoListItems by carritoDeCompraViewModel.itemCarrito.collectAsState()
    val totalPrecioSeleccionado by carritoDeCompraViewModel.totalPrecioSeleccionado.collectAsState(initial = 0.0)

    LaunchedEffect(key1 = carritoListItems.size) {
        carritoDeCompraViewModel.recargarCarrito()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carrito de Compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Centramos el contenido horizontalmente
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Total:", style = MaterialTheme.typography.headlineMedium)
                    Text(text = "${String.format("%.2f", totalPrecioSeleccionado)}€", style = MaterialTheme.typography.headlineMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("formularioCompra") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Comprar")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Log.d("CarritoDeCompraView", "Tamaño de la lista al componer: ${carritoListItems.size}")
            if (carritoListItems.isEmpty()) {
                Text(
                    "Tu carrito de compra está vacía.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(carritoListItems) { item ->
                        Log.d("CarritoDeCompra", "Item en la lista: ${item.name}, ID: ${item.id}")
                        CartItemRow(
                            item = item,
                            onRemoveFromCart = { carritoDeCompraViewModel.removeItemFromCarritolist(it) },
                            onAddToWishlist = { listaDeDeseosViewModel.addItemToWishlist(it) },
                            // onQuantityIncremented = { /* TODO: Implementar */ },
                            // onQuantityDecremented = { /* TODO: Implementar */ },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: ItemsModel,
    onRemoveFromCart: (ItemsModel) -> Unit,
    onAddToWishlist: (ItemsModel) -> Unit,
    // onQuantityIncremented: (ItemsModel) -> Unit,
    // onQuantityDecremented: (ItemsModel) -> Unit,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp) // Reducido el padding del item
    ) {
        // Imagen del producto (ocupa un espacio más pequeño)
        item.imageUrl.firstOrNull()?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp) // Tamaño más pequeño para la imagen
                    .padding(end = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f) // El texto ocupa el espacio restante
        ) {
            Text(text = item.name ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(text = "$${item.price ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            // Aquí podrías mostrar la cantidad si la implementas en tu modelo de carrito
        }

        // Botones
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onRemoveFromCart(item) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar del carrito")
            }
            IconButton(onClick = { onAddToWishlist(item) }) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Añadir a lista de deseos")
            }
            // Implementación básica de los botones de cantidad (puedes personalizar más)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* TODO: Implementar decremento de cantidad */ }) {
                    Icon(painterResource(id = R.drawable.remove), contentDescription = "Quitar uno")
                }
                Text(text = "1") // Reemplaza con la cantidad real del item en el carrito
                IconButton(onClick = { /* TODO: Implementar incremento de cantidad */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir uno")
                }
            }
        }
    }
}