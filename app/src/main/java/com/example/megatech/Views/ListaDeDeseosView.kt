package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ListaDeDeseosView(navController: NavController, listaDeDeseosViewModel: ListaDeDeseosViewModel,carritoDeCompraViewModel: CarritoDeCompraViewModel) {
    val wishlistItems by listaDeDeseosViewModel.wishlistItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de deseo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Log.d("CarritoDeCompraView", "Tamaño de la lista al componer: ${wishlistItems.size}")
            if (wishlistItems.isEmpty()) {
                Text(
                    "Tu carrito de compra está vacía.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(wishlistItems) { item ->
                        Log.d("CarritoDeCompra", "Item en la lista: ${item.name}, ID: ${item.id}")
                        WishItemRow(
                            item = item,
                            onRemoveFromWish = { listaDeDeseosViewModel.removeItemFromWishlist(it) },
                            onAddToCart = { carritoDeCompraViewModel.addItemToCarritolist(it) },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishItemRow(
    item: ItemsModel,
    onRemoveFromWish: (ItemsModel) -> Unit,
    onAddToCart: (ItemsModel) -> Unit,
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
            IconButton(onClick = { onRemoveFromWish(item) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar del carrito")
            }
            IconButton(onClick = { onAddToCart(item) }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir a lista de deseos")
            }
        }
    }
}