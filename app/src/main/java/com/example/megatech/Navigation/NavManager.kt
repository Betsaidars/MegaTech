package com.example.megatech.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.megatech.Components.DetalleProductoScreen
import com.example.megatech.SessionManager
import com.example.megatech.ViewModels.CarritoDeCompraViewModel
import com.example.megatech.ViewModels.CarritoDeCompraViewModelFactory
import com.example.megatech.ViewModels.ListaDeDeseosViewModel
import com.example.megatech.ViewModels.PedidoViewModel
import com.example.megatech.ViewModels.PedidoViewModelFactory
import com.example.megatech.Views.BannerCincuentaView
import com.example.megatech.Views.BannerTreintaView
import com.example.megatech.Views.CamaraView
import com.example.megatech.Views.CarritoDeCompraView
import com.example.megatech.Views.CompraRealizadaView
import com.example.megatech.Views.ConsolaView
import com.example.megatech.Views.FormularioCompraView
import com.example.megatech.Views.HogarInteligenteView
import com.example.megatech.Views.ListaDeDeseosView
import com.example.megatech.Views.MainView
import com.example.megatech.Views.MisPedidosView
import com.example.megatech.Views.OrdenadoresView
import com.example.megatech.Views.PhoneView
import com.example.megatech.Views.RelojesView
import com.example.megatech.Views.SonidoView
import com.example.megatech.Views.TabsView
import com.example.megatech.Views.TelevisoresView


@Composable
fun NavManager(sessionManager: SessionManager, listaDeDeseosViewModel: ListaDeDeseosViewModel, carritoDeCompraViewModel: CarritoDeCompraViewModel){

    val navController = rememberNavController()
    val context = LocalContext.current

    // Obtener el ViewModelStoreOwner asociado al ámbito del NavHost
    val viewModelStoreOwner = LocalViewModelStoreOwner.current!!

    // Crear el ViewModel asociado al NavController (su ciclo de vida)
    val pedidoViewModel: PedidoViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = PedidoViewModelFactory(context = context, sessionManager = sessionManager)
    )
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

        composable("television"){
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

        composable("listaDeDeseos") {
            ListaDeDeseosView(navController = navController, listaDeDeseosViewModel = listaDeDeseosViewModel, carritoDeCompraViewModel)
        }

        composable("DiscountTreinta") {
            BannerTreintaView(navController = navController)
        }

        composable("DiscountCincuenta") {
            BannerCincuentaView(navController = navController)
        }

        composable("carritoDeCompra") {
            CarritoDeCompraView(
                navController = navController,
                carritoDeCompraViewModel = carritoDeCompraViewModel,
                listaDeDeseosViewModel = listaDeDeseosViewModel,
                onComprarClicked = { navController.navigate("formularioCompra") }
            )
        }

        composable("formularioCompra") {
            FormularioCompraView(
                navController = navController,
                carritoDeCompraViewModel = viewModel(factory = CarritoDeCompraViewModelFactory(context)),
                sessionManager = sessionManager,
                pedidoViewModel = pedidoViewModel // Pasar la instancia
            )
        }

        composable("compraRealizada") {
            CompraRealizadaView(navController = navController)
        }

        composable("misPedidos") {
            MisPedidosView(
                navController = navController,
                sessionManager = sessionManager,
                pedidoViewModel = pedidoViewModel // Pasar la misma instancia
            )
        }

        composable(
            "detalleProducto/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DetalleProductoScreen(itemId = itemId, sessionManager, navController, listaDeDeseosViewModel, carritoDeCompraViewModel)
        }

        composable("itemDetail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DetalleProductoScreen(itemId = itemId, sessionManager = sessionManager, navController, listaDeDeseosViewModel, carritoDeCompraViewModel)
        }
    }
}