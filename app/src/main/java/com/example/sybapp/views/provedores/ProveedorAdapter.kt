package com.example.sybapp.views.provedores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sybapp.R
import com.example.sybapp.models.Producto
import com.example.sybapp.models.Proveedor


class ProveedorAdapter(
    var contexto: Context,
    var listaProveedores: ArrayList<Proveedor>,
    var onClick: OnItemClicked
): RecyclerView.Adapter<ProveedorAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tv_proveedor,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val proveedor = listaProveedores[position]

            holder.tvNombreProveedor.text = proveedor.nomProveedor

        holder.ibtnTelefono.setOnClickListener{
            onClick.llamarProveedor(proveedor.telefono)
        }

        holder.ibtnCorreo.setOnClickListener {
            onClick.enviarEmail(proveedor.email)
        }

        holder.ibtnEditar.setOnClickListener {
            onClick.editarProveedor(proveedor)
        }
    }

    override fun getItemCount(): Int {
        return listaProveedores.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvNombreProveedor: TextView = itemView.findViewById(R.id.tvNombreProveedor)
        val ibtnTelefono:ImageButton = itemView.findViewById(R.id.ibtnTelefono)
        val ibtnCorreo: ImageButton = itemView.findViewById(R.id.ibtnCorreo)
        val ibtnEditar: ImageButton = itemView.findViewById(R.id.ibtnEditar)

    }

    interface OnItemClicked{
        fun llamarProveedor(tel: String)
        fun enviarEmail(email:String)
        fun editarProveedor(prov: Proveedor)
    }
}