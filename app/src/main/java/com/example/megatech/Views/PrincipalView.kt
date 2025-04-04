package com.example.megatech.Views


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.megatech.Components.Banner
import com.example.megatech.Components.CategoriaProductos
import com.example.megatech.R

@Composable
fun PrincipalView(navController: NavController){

    Column {
        Banner(
            image = R.drawable.sale_banner,
            onClick = { navController.navigate("productos_rebajados") }
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(start = 10.dp, end = 10.dp)
        )
        CategoriaProductos(navController)
    }
}

















