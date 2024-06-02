package com.example.classtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import android.widget.ImageView

class HomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configurar el boton para retroceder
        val back: ImageView = findViewById(R.id.go_back_to_login)
        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón de añandir horario
        val addSchedule: Button = findViewById(R.id.add_schedule_button)
        addSchedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón de ver el horario
        val viewScheduleButton: Button = findViewById(R.id.view_schedule_button)
        viewScheduleButton.setOnClickListener {
            val intent = Intent(this, ViewScheduleActivity::class.java)
            startActivity(intent)
        }

    }
}