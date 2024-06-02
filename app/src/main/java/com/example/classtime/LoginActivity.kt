package com.example.classtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.content.Intent

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configurar botón de inicio de sesión
        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            // Lógica para manejar el inicio de sesión
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Configurar texto para ir a la pantalla de registro
        val signUpText: TextView = findViewById(R.id.sign_up_text)
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
