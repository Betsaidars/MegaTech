package com.example.megatech.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun DetalleProductoScreen(itemId: String?, sessionManager: SessionManager) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(sessionManager))
    val producto by mainViewModel.getItemById(itemId).collectAsState(initial = null)

    var selectedColor by remember { mutableStateOf<String?>(null) }
    val displayedImageUrl by remember(producto, selectedColor) {
        derivedStateOf {
            if (selectedColor.isNullOrEmpty()) {
                producto?.imageUrl?.firstOrNull()
            } else {
                // Lógica para obtener la URL de la imagen según el color seleccionado
                // Esto dependerá de cómo tu API maneja las imágenes por color.
                // Por ahora, mostraremos la primera imagen si no hay coincidencia específica.
                producto?.imageUrl?.firstOrNull()
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
            // Mostrar los detalles del producto
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

            Log.d("DETAIL_PRODUCT_CHECK", "Producto in DetalleProductoScreen: $producto")
            producto?.availableColors?.let { colors ->
                Log.d("DETAIL_COLORS_CHECK", "Available colors for ${producto?.name}: $colors")
                if (colors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Colores Disponibles:", fontWeight = FontWeight.Bold)
                    LazyRow(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(colors) { color ->
                            ColorCircle(
                                color = color,
                                isSelected = color == selectedColor,
                                onColorSelected = { selectedColor = it }
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