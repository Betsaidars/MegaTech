package com.example.megatech.Components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.megatech.R

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
fun CategoriaProductos(navController: NavController) {
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
                texto = "Tv",
                onClick = { navController.navigate("televisores") }
            )
            BotonesIcono(
                imagen = R.drawable.sonido,
                texto = "Sonido",
                onClick = { navController.navigate("sonido") }
            )
            BotonesIcono(
                imagen = R.drawable.phone,
                texto = "Smartphone",
                onClick = { navController.navigate("moviles") }
            )
            BotonesIcono(
                imagen = R.drawable.camara,
                texto = "Cámara",
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
                texto = "Consola",
                onClick = { navController.navigate("consolas") }
            )
            BotonesIcono(
                imagen = R.drawable.home,
                texto = "Hogar",
                onClick = { navController.navigate("hogar_inteligente") }
            )
            BotonesIcono(
                imagen = R.drawable.ordenador,
                texto = "Ordenador",
                onClick = { navController.navigate("ordenadores") }
            )
            BotonesIcono(
                imagen = R.drawable.reloj,
                texto = "Reloj",
                onClick = { navController.navigate("relojes") }
            )
        }
    }
    Text(
        text = "Más vendidos",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        fontSize = 25.sp,
        fontFamily = FontFamily.Serif,
        textAlign = TextAlign.Center
    )

}

//@Composable
//fun ProductosMasVendidos(productos: List<Producto>, navController: NavController){
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        /** TEXTO DE MÁS VENDIDOS **/
//
//        LazyColumn {
//            items(productos) { producto ->
//                ProductoItem(
//                    producto = producto,
//                    navController = navController
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ProductoItem(producto: Producto, navController: NavController){
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .clickable {
//                navController.navigate("detalle_producto/${producto.id}")
//            }
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.aur_gamen_ina_blancos),
//                contentDescription = producto.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentScale = ContentScale.Crop
//            )
//            Image(
//                painter = painterResource(id = R.drawable.camara_bridge_negro),
//                contentDescription = producto.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                contentScale = ContentScale.Crop
//            )
//            Row(modifier = Modifier
//                .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(text = producto.name)
//                Text(text = "$${producto.price}")
//            }
//        }
//    }
//}





















