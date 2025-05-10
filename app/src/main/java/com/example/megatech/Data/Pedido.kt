package com.example.megatech.Data


data class Pedido(
    val id: String,
    val fecha: String,
    val items: List<String>,
    val total: Double,
    val nombreUsuario: String,
    val direccionEnvio: String,
    val metodoPago: String,
    val codigoDescuento: String? = null
)