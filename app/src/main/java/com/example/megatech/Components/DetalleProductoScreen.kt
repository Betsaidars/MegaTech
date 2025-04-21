package com.example.megatech.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.MainViewModel
import com.example.megatech.ViewModels.MainViewModelFactory
import android.util.Log

@Composable
fun DetalleProductoScreen(itemId: String?, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val producto by mainViewModel.getItemById(itemId).collectAsState(initial = null)

    var selectedColorIndex by remember { mutableStateOf<Int?>(null) }
    val displayedImageUrl by remember(producto, selectedColorIndex) {
        derivedStateOf {
            if (selectedColorIndex != null && producto?.imageUrl?.isNotEmpty() == true) {
                producto!!.imageUrl.getOrNull(selectedColorIndex!!)
            } else {
                producto?.imageUrl?.firstOrNull() // Muestra la primera imagen por defecto
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Detalle del Producto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            Text(text = producto?.name ?: "", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = producto?.brand ?: "", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Descripción: ${producto?.description ?: ""}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Modelo: ${producto?.model ?: ""}", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Precio: $${producto?.price ?: ""}", fontWeight = FontWeight.Bold)

            producto!!.availableColors?.let { colors ->
                if (colors.isNotEmpty() && producto!!.imageUrl.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Colores Disponibles:", fontWeight = FontWeight.Bold)
                    LazyRow(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        itemsIndexed(colors) { index, colorName -> // Usamos itemsIndexed para obtener el índice
                            ColorCircle(
                                color = colorName,
                                isSelected = index == selectedColorIndex,
                                onColorSelected = {
                                    selectedColorIndex = index
                                    Log.d("COLOR_SELECTED", "Color: $colorName, Index: $index")
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
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

    val englishColorName = colorMap[color] ?: color // Usa el nombre en inglés si existe, sino usa el original

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