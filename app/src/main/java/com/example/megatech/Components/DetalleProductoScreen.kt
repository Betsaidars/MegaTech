package com.example.megatech.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.R
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.CarritoDeCompraViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModel
import com.example.megatech.ViewModels.ListaDeDeseosViewModelFactory
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory
import androidx.compose.material.icons.filled.Favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(itemId: String?, sessionManager: SessionManager, navController: NavHostController, listaDeDeseosViewModel: ListaDeDeseosViewModel, carritoDeCompraViewModel: CarritoDeCompraViewModel) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val producto by mainViewModel.getItemById(itemId).collectAsState(initial = null)

    val rosa = Color(0xFFEC407A)

    var selectedColorIndex by remember { mutableStateOf<Int?>(null) }

    var selectedColorName by remember { mutableStateOf<String?>(null) }

    val displayedImageUrl by remember(producto, selectedColorIndex) {
        derivedStateOf {
            if (selectedColorIndex != null && producto?.imageUrl?.isNotEmpty() == true) {
                producto!!.imageUrl.getOrNull(selectedColorIndex!!)
            } else {
                producto?.imageUrl?.firstOrNull()
            }
        }
    }

    val listaDeDeseos by listaDeDeseosViewModel.wishlistItems.collectAsState()
    val carritoDeCompra by carritoDeCompraViewModel.itemCarrito.collectAsState()

    var isFavoriteBottom by remember { mutableStateOf(false) }

    LaunchedEffect(producto, listaDeDeseos, selectedColorName) {
        isFavoriteBottom = producto?.let { p ->
            listaDeDeseosViewModel.isItemInWishlist(p.id, selectedColorName)
        } ?: false
    }


    var isCarritoBottom by remember(producto, carritoDeCompra) {
        mutableStateOf(producto?.id?.let { itemId ->
            carritoDeCompra.any { it.id == itemId }
        } ?: false)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Log.d("DetalleProductoScreen", "Tamaño de la lista en Detalle: ${listaDeDeseos.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        CenterAlignedTopAppBar(
            title = {  "" },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate("listaDeDeseos") }) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Añadir a deseados")
                }
                IconButton(onClick = { navController.navigate("carritoDeCompra") }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
            },
            scrollBehavior = scrollBehavior
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            if (producto == null) {
                Text("Cargando detalles del producto...")
            } else {
                displayedImageUrl?.let { url ->
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = producto!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Inside
                    )
                }


                producto!!.availableColors?.let { colors ->
                    if (colors.isNotEmpty() && producto!!.imageUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Colores Disponibles:", fontWeight = FontWeight.Bold)
                        LazyRow(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            itemsIndexed(colors) { index, colorName ->
                                ColorCircle(
                                    color = colorName,
                                    isSelected = index == selectedColorIndex,
                                    onColorSelected = { selected  ->
                                        selectedColorIndex = index
                                        selectedColorName = selected
                                        Log.d("COLOR_SELECTED", "Color: $colorName, Index: $index")
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = producto!!.name ?: "", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = producto!!.brand ?: "", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descripción: ${producto!!.description ?: ""}")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Modelo: ${producto!!.model ?: ""}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Precio: $${producto!!.price ?: ""}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                producto!!.rating?.let { ratingValue ->
                    RatingStars(rating = ratingValue)
                }
            }
        }

        // Botones en la parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    producto?.let {
                        Log.d("DEBUG_COLOR", "Intentando añadir al carrito. Color seleccionado: $selectedColorName")
                        val productoParaCarrito = it.copy(
                            selectedColor = selectedColorName
                        )
                        carritoDeCompraViewModel.addItemToCarritolist(productoParaCarrito)
                        isCarritoBottom = true // Actualizar el estado visual del carrito
                        Log.d("DetalleProducto", "Añadido al carrito: ${productoParaCarrito.id} con color: ${productoParaCarrito.selectedColor}")
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA7B1))
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = "Añadir al carrito",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Añadir al carrito", color = Color.White)
            }

            IconButton(
                onClick = {
                    producto?.let {
                        val productoParaDeseos = it.copy(
                            selectedColor = selectedColorName // Usa el color seleccionado aquí
                        )
                        isFavoriteBottom = !isFavoriteBottom
                        if (isFavoriteBottom) {
                            listaDeDeseosViewModel.addItemToWishlist(productoParaDeseos)
                            Log.d("DetalleProducto", "Añadido a la lista de deseos: ${productoParaDeseos.id} con color: ${productoParaDeseos.selectedColor}")                        } else {
                            listaDeDeseosViewModel.removeItemFromWishlist(productoParaDeseos)
                            Log.d("DetalleProducto", "Eliminado de la lista de deseos: ${it.id}")
                        }
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFavoriteBottom) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavoriteBottom) "Eliminar de deseados" else "Añadir a deseados",
                    modifier = Modifier.size(24.dp),
                    tint = if (isFavoriteBottom) Color.Black else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
@Composable
fun ColorCircle(color: String?, isSelected: Boolean, onColorSelected: (String?) -> Unit) {
    Log.d("COLOR_CIRCLE_CHECK", "Color received: $color, isSelected: $isSelected")

    val colorMap = remember {
        mapOf(
            "Azul" to "Blue",
            "Rosa" to "#FFC0CB",
            "Blanco" to "White",
            "Negro" to "Black",
            "Verde" to "Green",
            "Rojo" to "Red",
            "Lavanda" to "Purple",
            "Gris espacial" to "Grey",
            "Plateado" to "Silver",
            "Dorado" to "#FFD700",
            "Gris" to "Grey",
            "Amarillo" to "Yellow",
            "Turquesa" to "#40E0D0"
        )
    }

    val englishColorName = colorMap[color] ?: color

    val colorToDisplay = remember(englishColorName) {
        try {
            Color(android.graphics.Color.parseColor(englishColorName))
        } catch (e: IllegalArgumentException) {
            Log.e("COLOR_CIRCLE_ERROR", "Error parsing color: $englishColorName (original: $color)", e)
            Color.Gray
        }
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(colorToDisplay)
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onColorSelected(color) }
    )
}
