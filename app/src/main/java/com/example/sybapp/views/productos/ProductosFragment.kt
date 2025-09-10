package com.example.sybapp.views.productos

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sybapp.R
import com.example.sybapp.databinding.FragmentProductosBinding
import com.example.sybapp.models.Producto
import com.example.sybapp.models.ProductosViewModel
import com.example.sybapp.views.main.MainActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import es.dmoral.toasty.Toasty


class ProductosFragment : Fragment(R.layout.fragment_productos), ProductoAdapter.OnItemClicked {

    private lateinit var binding: FragmentProductosBinding
    private lateinit var adapter: ProductoAdapter
    private lateinit var viewModel: ProductosViewModel

    private lateinit var etCodigo: EditText

    private var codigoLeido = ""


    private val barcodeLouncher = registerForActivityResult(ScanContract()) { result ->
        codigoLeido = ""

        if (result.contents == null) {
            //No se hace nada
        } else {
            codigoLeido = result.contents.toString()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductosBinding.bind(view)

        (requireContext() as MainActivity).setToolbarTitle("PRODUCTOS")

        viewModel = ViewModelProvider(this)[ProductosViewModel::class.java]

        setupRecyclerView()


        viewModel.getProductos()

        viewModel.mensaje.observe(requireActivity()) { mensaje ->
            Toasty.info(requireActivity(), mensaje, Toasty.LENGTH_SHORT, true).show()
        }

        viewModel.listaProductos.observe(viewLifecycleOwner) { lista ->
            adapter.listaProductos = lista as ArrayList<Producto>

            adapter.notifyDataSetChanged()
        }

        binding.ibtnBuscar.setOnClickListener{
            codigoLeido = ""
            barcodeLouncher.launch(ScanOptions())
            binding.etBusqueda.setText(codigoLeido)
        }

        binding.etBusqueda.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
               //Acción después del cambio
                if (p0.isNullOrEmpty()){
                    viewModel.getProductos()

                }else{
                    viewModel.filtrarListaProductos(p0.toString().trim())
                }

                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Acción antes del cambio

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Acción durante el cambio
            }

        })

        binding.ibtnAgregar.setOnClickListener {
            alertDialogAddUpdate("add")
        }
    }

    private fun setupRecyclerView() {
        binding.rvProductos.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductoAdapter(arrayListOf(), this)
        binding.rvProductos.adapter = adapter
    }

    private fun alertDialogAddUpdate(
        accion: String,
        idProducto: String = "",
        nomProducto: String = "",
        descripcion: String = "",
        nomProveedor: String = "",
        almacen: Int = -1,
        precio: Double = -1.0

    ) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val vista = inflater.inflate(R.layout.alert_dialog_producto, null)
        builder.setView(vista)

        if (accion == "add") {
            builder.setTitle("Agregar Producto")
        }
        else{
            builder.setTitle("Editando producto")
        }

        builder.setCancelable(false)

        etCodigo = vista.findViewById(R.id.etCodigo)
        val ibtnEscaner = vista.findViewById<ImageButton>(R.id.ibtnEscaner)
        val etNomProducto = vista.findViewById<EditText>(R.id.etNomProducto)
        val etDescripcion = vista.findViewById<EditText>(R.id.etDescripcion)
        val spiProveedor = vista.findViewById<Spinner>(R.id.spiProveedor)
        val etPrecio = vista.findViewById<EditText>(R.id.etPrecio)
        val etAlmacen = vista.findViewById<EditText>(R.id.etAlmacen)


        viewModel.getProveedores()
        viewModel.listaNomProveedores.observe(viewLifecycleOwner) {
            spiProveedor.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            )
        }

        ibtnEscaner.setOnClickListener {
            val isPermiso = viewModel.checkCamaraPermiso(requireActivity())
            if (isPermiso) {
                barcodeLouncher.launch(ScanOptions())
                etCodigo.setText(codigoLeido)
            }
        }
        if(accion == "update"){
            etCodigo.setText(idProducto)
            etCodigo.isEnabled = false
            ibtnEscaner.visibility = View.GONE
            etNomProducto.setText(nomProducto)
            etDescripcion.setText(descripcion)
            spiProveedor.selectedItem
            etAlmacen.setText(almacen.toString())
            etPrecio.setText(precio.toString())
        }

        builder.setPositiveButton("ACEPTAR") { dialog, _ ->
            viewModel.validarCampos(
                accion,
                etCodigo.text.toString().trim(),
                etNomProducto.text.toString().trim(),
                spiProveedor.selectedItem.toString(),
                etDescripcion.text.toString().trim(),
                etPrecio.text.toString().trim(),
                etAlmacen.text.toString().trim()
            )
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("CANCELAR") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun editarProducto(prod: Producto) {
        alertDialogAddUpdate(
            "update",
            prod.codProducto,
            prod.nomProducto,
            prod.descripcion,
            prod.nomProveedor,
            prod.almacen,
            prod.precio
        )
    }
}

