package com.example.megatech.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Data.Pedido
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PedidoViewModel(private val context: Context, private val sessionManager: SessionManager) : ViewModel() {

    private val _listaPedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val listaPedidos: StateFlow<List<Pedido>> = _listaPedidos

    private val _listaDePedidosGuardados = MutableStateFlow<MutableList<Pedido>>(mutableListOf()) // Lista mutable temporal


    init {
        cargarPedidosDelUsuario()
    }

    private fun cargarPedidosDelUsuario() {
        // Ahora simplemente emitimos la lista de pedidos guardados
        _listaPedidos.value = _listaDePedidosGuardados.value.toList() // Emitimos una copia inmutable
        Log.d("PedidoViewModel", "Pedidos cargados: ${_listaPedidos.value}")
    }

    fun guardarNuevoPedido(nombreUsuario: String, direccionEnvio: String, items: List<String>, total: Double,  metodoPago: String) {
        Log.d("PedidoViewModel", "guardarNuevoPedido llamado con: Nombre=$nombreUsuario, Dirección=$direccionEnvio, Items=$items, Total=$total")
        val nuevoPedido = Pedido(
            id = UUID.randomUUID().toString(),
            fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            items = items,
            total = total,
            nombreUsuario = nombreUsuario,
            direccionEnvio = direccionEnvio,
            metodoPago = metodoPago
        )
        // Usa update para actualizar el StateFlow de forma segura y reactiva
        _listaPedidos.update { currentList ->
            currentList + nuevoPedido
        }
        Log.d("PedidoViewModel", "Pedido añadido a la lista: ${_listaPedidos.value}")
    }



}
