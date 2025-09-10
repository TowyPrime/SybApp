package com.example.sybapp.models

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sybapp.network.response.ProductoResponse
import com.example.sybapp.network.response.RetrofitClient
import com.example.sybapp.utils.Permisos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductosViewModel : ViewModel() {

    // PRODUCTOS
    private var _listaProductos = MutableLiveData<MutableList<Producto>>()
    val listaProductos: LiveData<MutableList<Producto>> get() = _listaProductos

    // PROVEEDOR
    private var _listaNomProveedores = MutableLiveData<ArrayList<String>>()
    val listaNomProveedores: LiveData<ArrayList<String>> get() = _listaNomProveedores

    // MENSAJES
    private var _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private lateinit var response: Response<ProductoResponse>

    // Obtener productos
    fun getProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getProductos()
                withContext(Dispatchers.Main) {
                    val body = response.body()
                    if (body != null && body.codigo == "200") {
                        _listaProductos.value = body.resultado
                    } else {
                        _mensaje.value = body?.mensaje ?: "Error al obtener productos"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mensaje.value = "Error de red: ${e.message}"
                }
            }
        }
    }

    // Obtener proveedores
    fun getProveedores() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getProveedores()
                withContext(Dispatchers.Main) {
                    val body = response.body()
                    if (body != null && body.codigo == "200") {
                        val aux = ArrayList<String>()
                        body.resultado.forEach {
                            aux.add(it.nomProveedor)
                        }
                        _listaNomProveedores.value = aux
                    } else {
                        _mensaje.value = body?.mensaje ?: "Error al obtener proveedores"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mensaje.value = "Error de red: ${e.message}"
                }
            }
        }
    }

    // Filtrar productos
    fun filtrarListaProductos(producto: String) {
        val listaActual = _listaProductos.value ?: return
        val listaFiltrada: MutableList<Producto> = mutableListOf()
        for (prod in listaActual) {
            if (prod.codProducto.contains(producto) || prod.nomProducto.contains(producto)) {
                listaFiltrada.add(prod)
            }
        }
        _listaProductos.value = listaFiltrada
    }

    // Validar campos antes de agregar/editar
    fun validarCampos(
        accion: String,
        codigo: String,
        nomProducto: String,
        nomProveedor: String,
        descripcion: String,
        precio: String,
        almacen: String
    ) {
        if (
            codigo.isEmpty() ||
            nomProducto.isEmpty() ||
            descripcion.isEmpty() ||
            nomProveedor.isEmpty() ||
            precio.isEmpty() ||
            almacen.isEmpty()
        ) {
            _mensaje.value = "Todos los campos deben ser llenados"
        } else {
            val precioDouble = precio.toDoubleOrNull()
            val almacenInt = almacen.toIntOrNull()

            if (precioDouble == null || almacenInt == null) {
                _mensaje.value = "Precio y Almacén deben ser valores numéricos"
                return
            }

            val prod = Producto(
                codigo,
                nomProducto,
                descripcion,
                nomProveedor,
                precioDouble,
                almacenInt
            )
            productoAddUpdate(accion, prod)
        }
    }

    // Agregar o actualizar producto
    private fun productoAddUpdate(accion: String, prod: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                response = if (accion == "add") {
                    RetrofitClient.webService.addProducto(prod)
                } else {
                    RetrofitClient.webService.updateProducto(prod.codProducto, prod)
                }

                withContext(Dispatchers.Main) {
                    val body = response.body()
                    if (body != null && body.codigo == "200") {
                        _mensaje.value = body.mensaje
                        getProductos()
                    } else {
                        _mensaje.value = body?.mensaje ?: "Error en la respuesta del servidor"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mensaje.value = "Error de red: ${e.message}"
                }
            }
        }
    }

    fun checkCamaraPermiso(activity: Activity): Boolean {
        return Permisos().checkCamaraPermiso(activity)
    }
}
