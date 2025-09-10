package com.example.sybapp.views.provedores


import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sybapp.R
import com.example.sybapp.databinding.FragmentProvedoresBinding
import com.example.sybapp.models.Proveedor
import androidx.core.net.toUri
import com.example.sybapp.views.main.MainActivity
import es.dmoral.toasty.Toasty



class ProvedoresFragment : Fragment(R.layout.fragment_provedores), ProveedorAdapter.OnItemClicked {

    private lateinit var binding: FragmentProvedoresBinding
    private lateinit var adapter: ProveedorAdapter
    private lateinit var viewModel:ProveedoresViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProvedoresBinding.bind(view)
        (requireContext() as MainActivity).setToolbarTitle("PROVEEDORES")


        viewModel = ViewModelProvider(this)[ProveedoresViewModel::class.java]

        setupRecyclerView()
        viewModel.getProveedores()

        viewModel.listaProveedores.observe(requireActivity()){
            adapter.listaProveedores = it as ArrayList<Proveedor>
            adapter.notifyDataSetChanged()
        }

        viewModel.mensaje.observe(requireActivity()){mensaje ->
            Toasty.info(requireContext(),mensaje,Toasty.LENGTH_SHORT).show()
        }

        binding.ibtnAgregar.setOnClickListener {
            alertDialogAddUpdate("add")
        }
    }


    private fun setupRecyclerView(){
        binding.rvProveedores.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ProveedorAdapter(requireContext(), arrayListOf(),this)
        binding.rvProveedores.adapter = adapter
    }

    private fun alertDialogAddUpdate(accion:String){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        val vista = inflater.inflate(R.layout.alert_dialog_proveedor,null)

        if (accion == "add"){
            builder.setTitle("Agregar proveedor")
        }

        val etNomProveedor: EditText = vista.findViewById(R.id.etNomProveedor)
        val etTelefono: EditText = vista.findViewById(R.id.etTelefono)
        val etEmail: EditText = vista.findViewById(R.id.etEmail)

        builder.setView(vista)
        builder.setCancelable(false)

        builder.setPositiveButton("ACEPTAR"){_, _ ->
            viewModel.validarCampos(accion,
                etNomProveedor.text.toString().trim(),
                etEmail.text.toString().trim(),
                etTelefono.text.toString().trim()
            )
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("CANCELAR"){_, _ ->

        }

        builder.show()
    }

    override fun llamarProveedor(tel: String) {
        val intent= Intent(Intent.ACTION_DIAL)
        intent.data = "tel: $tel".toUri()
        requireContext().startActivity(intent)
    }

    override fun enviarEmail(email: String) {
        val to = arrayOf(email)
         val cc = arrayOf("")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = "mailto:".toUri()
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL,to)
        emailIntent.putExtra(Intent.EXTRA_CC,cc)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Asunto")
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Escribe tu mensaje aqui:")

        try {
            requireContext().startActivity(Intent.createChooser(emailIntent,"Enviar email"))
        }catch (ex:ActivityNotFoundException){
            Toasty.error(requireContext(),"No tienes ningún gestor de correos instalado",Toasty.LENGTH_SHORT).show()

        }
    }

    override fun editarProveedor(prov: Proveedor) {
        TODO("Not yet implemented")
    }
}