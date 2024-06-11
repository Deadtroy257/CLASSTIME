package com.example.classtime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

class ScheduleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val back: ImageView = findViewById(R.id.go_back_to_home)
        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val titleEditText: TextInputEditText = findViewById(R.id.title)
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val addButton = findViewById<Button>(R.id.add_schedule_button)
        val daysOfWeek = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val hoursOfDay = arrayOf("8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 1:00")

        addHeaders(tableLayout, daysOfWeek)
        addRows(tableLayout, daysOfWeek, hoursOfDay)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString() // Mover aquí para capturar el título en el momento de guardar
            saveSchedule(tableLayout, daysOfWeek, hoursOfDay, title)
        }
    }

    private fun addHeaders(tableLayout: TableLayout, daysOfWeek: Array<String>) {
        val headerRow = TableRow(this)
        headerRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val emptyHeader = TextView(this)
        emptyHeader.setPadding(16, 16, 16, 16)
        headerRow.addView(emptyHeader)

        for (day in daysOfWeek) {
            val header = TextView(this)
            header.text = day
            header.gravity = Gravity.CENTER
            header.setPadding(16, 16, 16, 16)
            header.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            header.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            headerRow.addView(header)
        }
        tableLayout.addView(headerRow)
    }

    private fun addRows(tableLayout: TableLayout, daysOfWeek: Array<String>, hoursOfDay: Array<String>) {
        for (hour in hoursOfDay) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            val hourText = TextView(this)
            hourText.text = hour
            hourText.setPadding(16, 16, 16, 16)
            hourText.setBackgroundResource(R.drawable.cell_border)
            hourText.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            hourText.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            tableRow.addView(hourText)

            for (dayIndex in daysOfWeek.indices) {
                val cell = EditText(this)
                cell.id = View.generateViewId()
                cell.tag = "$hour-$dayIndex"
                cell.setBackgroundResource(R.drawable.cell_border)
                cell.gravity = Gravity.CENTER
                cell.setPadding(16, 16, 16, 16)
                cell.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                cell.setTextColor(ContextCompat.getColor(this, R.color.black))
                tableRow.addView(cell)
            }
            tableLayout.addView(tableRow)
        }
    }

    private fun saveSchedule(tableLayout: TableLayout, daysOfWeek: Array<String>, hoursOfDay: Array<String>, title: String) {
        val scheduleData = mutableMapOf<String, Map<String, String>>()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 1 until tableLayout.childCount) { // Saltar la fila de encabezado
            val row = tableLayout.getChildAt(i) as TableRow
            val hourText = (row.getChildAt(0) as TextView).text.toString()

            val daySchedule = mutableMapOf<String, String>()
            for (j in 1 until row.childCount) {
                val cell = row.getChildAt(j) as EditText
                val cellText = cell.text.toString()
                if (cellText.isNotEmpty()) {
                    val day = daysOfWeek[j - 1]
                    daySchedule[day] = cellText  // Corregir la asignación para guardar en el mapa por día
                }
            }
            scheduleData[hourText] = daySchedule  // Asignar el mapa de día al horario correspondiente
        }
        Log.d("ScheduleActivity", "Saving schedule: $scheduleData")

        // Agregar la fecha de creación
        val creationDate = Date()

        // Crear un mapa para los datos del horario
        val scheduleInfo = hashMapOf(
            "title" to title,
            "schedule" to scheduleData,
            "creationDate" to creationDate
        )

        // Obtener una referencia a la colección "schedules" del usuario actual, pero crear un nuevo documento con una ID única
        val userSchedulesRef = firestore.collection("schedules").document(currentUser.uid).collection("userSchedules").document(UUID.randomUUID().toString())

        userSchedulesRef.set(scheduleInfo)
            .addOnSuccessListener {
                Log.d("ScheduleActivity", "Horario guardado con éxito")
                Toast.makeText(this, "Horario guardado con éxito", Toast.LENGTH_SHORT).show()
                 val intent = Intent(this, ViewScheduleActivity::class.java)
                 startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.e("ScheduleActivity", "Error al guardar el horario", e)
                Toast.makeText(this, "Error al guardar el horario", Toast.LENGTH_SHORT).show()
            }
    }
}

