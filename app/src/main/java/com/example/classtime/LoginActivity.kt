package com.example.classtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar botón de inicio de sesión
        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener {

            val emailEditText: TextInputEditText = findViewById(R.id.user)
            val passwordEditText: TextInputEditText = findViewById(R.id.password)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                Toast.makeText(this, "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar texto para ir a la pantalla de registro
        val signUpText: TextView = findViewById(R.id.sign_up_text)
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Terminar LoginActivity para evitar que el usuario vuelva atrás
                } else {
                    // Si el inicio de sesión falla, mostrar un mensaje al usuario
                    Toast.makeText(baseContext, "Usuario/Contraseña incorrectos.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
