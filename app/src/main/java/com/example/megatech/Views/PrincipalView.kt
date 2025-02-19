package com.example.megatech.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.megatech.Components.BotonesIcono
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
    }
}

@Composable
fun Banner(image: Int, onClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
fun CategoriaProductos(navController: NavController){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BotonesIcono(
                imagen = R.drawable.televicion,
//                texto = "Tv",
                onClick = { navController.navigate("televisores") }
            )
            BotonesIcono(
                imagen = R.drawable.sonido,
//                texto = "Sonido",
                onClick = { navController.navigate("sonido") }
            )
            BotonesIcono(
                imagen = R.drawable.phone,
//                texto = "Smartphone",
                onClick = { navController.navigate("moviles") }
            )
            BotonesIcono(
                imagen = R.drawable.camara,
//                texto = "Cámara",
                onClick = { navController.navigate("camaras") }
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BotonesIcono(
                imagen = R.drawable.consola,
//                texto = "Consolas",
                onClick = { navController.navigate("consolas") }
            )
            BotonesIcono(
                imagen = R.drawable.home,
//                texto = "Hogar inteligente",
                onClick = { navController.navigate("hogar_inteligente") }
            )
            BotonesIcono(
                imagen = R.drawable.ordenador,
//                texto = "Ordenadores",
                onClick = { navController.navigate("ordenadores") }
            )
            BotonesIcono(
                imagen = R.drawable.reloj,
//                texto = "Relojes",
                onClick = { navController.navigate("relojes") }
            )
        }
    }
}














