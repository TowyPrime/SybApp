package com.example.sybapp.network.response

import com.example.sybapp.models.Producto

data class ProductoResponse(
    val codigo: String,
    val mensaje: String,
    val resultado: MutableList<Producto>
)
