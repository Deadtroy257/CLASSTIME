package com.example.classtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.content.Intent

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Configurar botón de registro
        val signUpButton: Button = findViewById(R.id.signup_button)
        signUpButton.setOnClickListener {
            // Lógica para manejar el registro
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Configurar texto para ir a la pantalla de inicio de sesión
        val loginText: TextView = findViewById(R.id.login_text)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
