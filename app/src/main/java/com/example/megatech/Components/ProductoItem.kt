package com.example.megatech.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.megatech.Model.ItemsModel

@Composable
fun ProductoItem(producto: ItemsModel, navController: NavController) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("detalleProducto/${producto.id}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        producto.imageUrl.firstOrNull()?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = producto.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } ?: Spacer(modifier = Modifier.size(80.dp)) // Espacio si no hay imagen

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = producto.name, fontWeight = FontWeight.Bold)
            Text(text = "$${producto.price}")
        }
    }
}