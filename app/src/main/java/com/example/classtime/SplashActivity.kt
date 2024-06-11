package com.example.classtime

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private val splashScreenDuration = 3000L // Duración en milisegundos (3 segundos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Iniciar una coroutine para el temporizador
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashScreenDuration)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}
