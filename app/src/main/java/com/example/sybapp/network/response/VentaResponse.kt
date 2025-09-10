package com.example.sybapp.network.response

import com.example.sybapp.models.DatosVentas

data class VentaResponse(
    val codigo: String,
    val mensaje: String,
    val resultado: List<DatosVentas>
)
