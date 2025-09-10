package com.example.sybapp.views.main


import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sybapp.R
import com.example.sybapp.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvTitulo: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin:TextView
    private lateinit var btnUsuario:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvTitulo = findViewById(R.id.tvTitulo)
        auth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.tvLogin)
        btnUsuario = findViewById(R.id.btnUsuario)

        setupNavegacion()


    }


    private fun setupNavegacion() {
        binding.bottomNavigationView.itemIconTintList = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )
    }

    fun setToolbarTitle(titulo: String) {
        tvTitulo.text = titulo
    }

    private fun login(email: String, password: String) {
        val tvCorreo: TextInputLayout = findViewById(R.id.tvCorreo)
        val tvPassword: TextInputLayout = findViewById(R.id.tvPassword)

        if (email != "" || password != "") {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toasty.info(
                        this, "Las credenciales son correctas",
                        Toasty.LENGTH_SHORT
                    ).show()
                    btnLogin.visibility = View.GONE
                    btnUsuario.visibility = View.VISIBLE
                    tvCorreo.error = null
                    tvPassword.error = null
                }
                .addOnFailureListener {
                    tvCorreo.error = "Fallo de credenciales"
                    tvPassword.error= "Fallo de credenciales"
                    Toasty.error(
                        this, "Las credenciales ingresadas son incorrectas",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
        } else {

            Toasty.error(
                this, "Los campos deben ser llenados correctamente",
                Toasty.LENGTH_SHORT
            ).show()
        }
    }


}
