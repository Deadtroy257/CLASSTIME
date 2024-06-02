package com.example.classtime

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Iniciar la actividad de inicio de sesión cuando se cree la actividad principal
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // Finalizar la actividad principal para que no se pueda volver atrás desde la pantalla de inicio de sesión
        finish()
    }
}