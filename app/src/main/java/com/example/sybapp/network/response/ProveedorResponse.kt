package com.example.sybapp.network.response

import com.example.sybapp.models.Proveedor

data class ProveedorResponse(
    val codigo: String,
    val mensaje: String,
    val resultado:MutableList<Proveedor>
)
