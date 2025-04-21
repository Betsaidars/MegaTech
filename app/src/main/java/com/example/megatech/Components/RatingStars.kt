package com.example.megatech.Components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.megatech.R

@Composable
fun RatingStars(rating: Double) {
    Row {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5

        // Mostrar estrellas llenas
        for (i in 0 until fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Estrella llena",
                tint = Color.Yellow,
                modifier = androidx.compose.ui.Modifier.size(20.dp)
            )
        }

        // Mostrar media estrella si es necesario
        if (hasHalfStar) {
            Icon(
                painterResource(id = R.drawable.star_half),
                contentDescription = "Media estrella",
                tint = Color.Yellow,
                modifier = androidx.compose.ui.Modifier.size(20.dp)
            )
        }

        // Mostrar estrellas vacías restantes
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0
        for (i in 0 until emptyStars) {
            Icon(
                painterResource(id = R.drawable.star_border),
                contentDescription = "Estrella vacía",
                tint = Color.Gray,
                modifier = androidx.compose.ui.Modifier.size(20.dp)
            )
        }
    }
}