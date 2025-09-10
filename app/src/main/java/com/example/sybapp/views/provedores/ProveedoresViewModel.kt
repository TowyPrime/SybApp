package com.example.sybapp.views.provedores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sybapp.models.Proveedor
import com.example.sybapp.network.response.ProveedorResponse
import com.example.sybapp.network.response.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProveedoresViewModel : ViewModel() {
    private var _listaProveedores = MutableLiveData<MutableList<Proveedor>>()
    val listaProveedores: LiveData<MutableList<Proveedor>> get() = _listaProveedores

    private var _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private lateinit var response: Response<ProveedorResponse>

    fun getProveedores() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.webService.getProveedores()
                withContext(Dispatchers.Main) {
                    val body = response.body()
                    if (body != null && body.codigo == "200") {
                        _listaProveedores.value = response.body()?.resultado
                    } else {
                        _mensaje.value = response.body()?.mensaje
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mensaje.value = "Error de red: ${e.message}"
                }
            }

        }
    }

    fun filtrarListaProveedores(nomProveedor: String) {
        val listaFiltrada: MutableList<Proveedor> = mutableListOf()
        for (proveedor in listaProveedores.value!!) {
            if (proveedor.nomProveedor.contains(nomProveedor)) {
                listaFiltrada.add(proveedor)
            }
        }
        _listaProveedores.value = listaFiltrada
    }

    fun validarCampos(accion: String,nomProv: String,email: String,tel: String) {
        if (nomProv.isNullOrEmpty() || email.isNullOrEmpty() || tel.isNullOrEmpty()) {
        _mensaje.value = "Todos los campos deben ser llenados"
        }else{
            val prov = Proveedor(email,nomProv,tel)
            proveedorAddUpdate(accion,prov)
        }

    }

    private fun proveedorAddUpdate(accion:String,prov:Proveedor){
        viewModelScope.launch(Dispatchers.IO) {
            try {
            if (accion == "add"){
                response = RetrofitClient.webService.addProveedor(prov)
            }else if(accion == "update"){
                response = RetrofitClient.webService.updateProveedor(prov.nomProveedor,prov)
            }

                withContext(Dispatchers.Main){
                    if (response.body() != null && response.body()?.codigo =="200"){
                        _mensaje.value = response.body()?.mensaje
                        getProveedores()
                    }else{
                        _mensaje.value = response.body()?.mensaje
                    }
                }

            }catch (e:Exception){
                _mensaje.value = "Error de red: ${e.message}"
            }
        }
    }

}