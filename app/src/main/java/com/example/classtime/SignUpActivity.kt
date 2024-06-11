package com.example.classtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        // Obtener referencias de los elementos de UI
        val signUpButton: Button = findViewById(R.id.signup_button)
        val emailEditText: TextInputEditText = findViewById(R.id.user)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)
        val confirmPasswordEditText: TextInputEditText = findViewById(R.id.confirm_password)


        // Configurar el botón de registro
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                signUp(email, password, confirmPassword)
            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el texto para ir a la pantalla de inicio de sesión
        val loginText: TextView = findViewById(R.id.login_text)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun signUp(email: String, password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            // Las contraseñas coinciden, intenta registrar el usuario en Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // El usuario se registró exitosamente
                        val user = mAuth.currentUser
                        // Aquí podrías realizar acciones adicionales, como enviar un correo de verificación
                        Toast.makeText(
                            baseContext, "Usuario registrado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Redirigir a la página de inicio
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        // Si hubo un error durante el registro, muestra un mensaje al usuario
                        Toast.makeText(
                            baseContext, "Error al registrar usuario: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Las contraseñas no coinciden, muestra un mensaje de error
            Toast.makeText(
                baseContext, "Las contraseñas no coinciden",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

