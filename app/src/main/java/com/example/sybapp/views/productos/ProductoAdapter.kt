package com.example.sybapp.views.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sybapp.R
import com.example.sybapp.models.Producto

class ProductoAdapter(
    var listaProductos: ArrayList<Producto>,
    val onClick: OnItemClicked
): RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tv_producto,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.tvIdProducto.text = producto.codProducto
        holder.tvnomProducto.text = "Producto: ${producto.nomProducto}"
        holder.tvDescripcion.text = "Descripcion: ${producto.descripcion}"
        holder.tvnomProveedor.text = "Proveedor: ${producto.nomProveedor}"
        holder.tvAlmacen.text = producto.almacen.toString()
        holder.tvPrecio.text = producto.precio.toString()

        holder.cvProducto.setOnClickListener {
            onClick?.editarProducto(producto)
        }

    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val cvProducto = itemView.findViewById(R.id.cvProducto) as CardView
        val tvIdProducto = itemView.findViewById(R.id.tcIdProducto) as TextView
        val tvnomProducto = itemView.findViewById(R.id.tvNomProducto) as TextView
        val tvDescripcion = itemView.findViewById(R.id.tvDescripcion) as TextView
        val tvnomProveedor = itemView.findViewById(R.id.tvNomProveedor)as TextView
        val tvAlmacen = itemView.findViewById(R.id.tvAlmacen) as TextView
        val tvPrecio = itemView.findViewById(R.id.tvPrecio)as TextView
    }

    interface OnItemClicked{
        fun editarProducto(prod:Producto)
    }
}