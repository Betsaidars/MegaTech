package com.example.megatech.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.megatech.Components.DetalleProductoScreen
import com.example.megatech.SessionManager
import com.example.megatech.Views.CamaraView
import com.example.megatech.Views.ConsolaView
import com.example.megatech.Views.DiscountView
import com.example.megatech.Views.HogarInteligenteView
import com.example.megatech.Views.MainView
import com.example.megatech.Views.OrdenadoresView
import com.example.megatech.Views.PhoneView
import com.example.megatech.Views.RelojesView
import com.example.megatech.Views.SonidoView
import com.example.megatech.Views.TabsView
import com.example.megatech.Views.TelevisoresView


@Composable
fun NavManager(sessionManager: SessionManager){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {

        composable("Login") {
            TabsView(navController, sessionManager)
        }

        composable("Register") {
            TabsView(navController, sessionManager)
        }

        composable("Main"){
            MainView(navController, sessionManager)
        }

//        composable("Discount") { backStackEntry ->
//            val discountPercentage = backStackEntry.arguments?.getString("discountPercentage")?.toIntOrNull() ?: 30 // Default 30%
//            DiscountView(navController, sessionManager, discountPercentage)
//        }

        composable("televicion"){
            TelevisoresView(navController, sessionManager)
        }

        composable("consola"){
            ConsolaView(navController, sessionManager)
        }

        composable("ordenador"){
            OrdenadoresView(navController, sessionManager)
        }

        composable("home"){
            HogarInteligenteView(navController, sessionManager)
        }

        composable("camara"){
            CamaraView(navController, sessionManager)
        }

        composable("sonido"){
            SonidoView(navController, sessionManager)
        }

        composable("phone"){
            PhoneView(navController, sessionManager)
        }

        composable("reloj"){
            RelojesView(navController, sessionManager)
        }

        composable(
            "detalleProducto/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DetalleProductoScreen(itemId = itemId, sessionManager) // Crea este Composable
        }


    }
}
