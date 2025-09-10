package com.example.sybapp.views.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sybapp.R
import com.example.sybapp.databinding.ActivitySplashScreenBinding
import com.example.sybapp.utils.Constantes
import com.example.sybapp.views.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Ocultar action bar
        supportActionBar?.hide()

        //Ocultar barra de estado
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Definir icóno para el splashScreen
        Glide.with(this).load(R.drawable.dispositivos).centerCrop().into(binding.ivSplashScreen)

        cambiarPantalla()

        }
    private fun cambiarPantalla(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, Constantes.DURACION_SPLASH_SCREEN)
    }

    }
