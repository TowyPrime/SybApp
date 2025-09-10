package com.example.sybapp.models

data class Producto(
    val codProducto: String,
    val nomProducto:String,
    val descripcion: String,
    val nomProveedor: String,
    val precio: Double,
    val almacen: Int
)

data class ProductoVenta(
    val codProducto: String,
    val nomProducto:String,
    val descripcion: String,
    val nomProvedor: String,
    val precio: Double,
    val almacen: Int,
    val cantidad:Int
)
