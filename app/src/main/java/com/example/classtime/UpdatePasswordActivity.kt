package com.example.classtime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar el botón para retroceder
        val back: ImageView = findViewById(R.id.go_back_to_home)
        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón de actualización de contraseña
        val updatePasswordButton: Button = findViewById(R.id.update_password_button)
        updatePasswordButton.setOnClickListener {
            val currentUser = auth.currentUser
            val currentPasswordEditText: TextInputEditText = findViewById(R.id.current_password)
            val newPasswordEditText: TextInputEditText = findViewById(R.id.new_password)

            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            if (currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                updatePassword(currentUser, currentPassword, newPassword)
            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }


        }
    }


    private fun updatePassword(currentUser: FirebaseUser?, currentPassword: String, newPassword: String) {
        if (currentUser != null) {
            val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword)

            // Reautenticar al usuario
            currentUser.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Actualizar la contraseña
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Contraseña actualizada correctamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish() // Cerrar la actividad después de una actualización exitosa
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error al actualizar la contraseña",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Error de autenticación: la contraseña actual es incorrecta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
