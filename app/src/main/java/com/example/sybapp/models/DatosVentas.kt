package com.example.sybapp.models

data class DatosVentas(
    val fechaVenta:String,
    val idVenta: String,
    val codProductos: List<ProdsVenta>,
    val total: Double
)
