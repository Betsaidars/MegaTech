package com.example.megatech.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.DiscountViewModel
import com.example.megatech.ViewModels.DiscountViewModelFactory

@Composable
fun DiscountView(navController: NavController, sessionManager: SessionManager, discountPercentage: Int){
    val discountViewModel: DiscountViewModel = viewModel(factory = DiscountViewModelFactory(sessionManager))

    val discountedItems = discountViewModel.discountedItems.collectAsState().value
    Log.d("DiscountView", "Items con descuento: ${discountedItems.size}")

    LaunchedEffect(discountPercentage) {
        discountViewModel.setDiscountPercentage(discountPercentage)
        if (discountViewModel.items.value.isEmpty()) {
            discountViewModel.getAllItems()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Mostrar el título según el descuento
        Text(
            text = "Productos con ${discountPercentage}% de descuento",
            modifier = Modifier.padding(16.dp)
        )

        // Mostrar los productos con el descuento aplicado
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(discountedItems) { item ->
                Log.d("DiscountView", "Mostrando producto: ${item.name}, Precio con descuento: ${item.price}")
                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("itemDetail/${item.id}")
                        }
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(item.imageUrl.firstOrNull() ?: "URL_DE_IMAGEN_DEFAULT"),
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "${item.price}€",  // Mostrar el precio con descuento
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }


}