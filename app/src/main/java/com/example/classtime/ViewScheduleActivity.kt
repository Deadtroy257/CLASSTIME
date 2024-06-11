package com.example.classtime

import android.content.Intent
import android.os.Bundle
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
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewScheduleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var scheduleSpinner: Spinner
    private lateinit var tableLayout: TableLayout
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var titleEditText: TextInputEditText

    private val daysOfWeek = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    private val hoursOfDay = arrayOf("8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 1:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_schedule)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        scheduleSpinner = findViewById(R.id.scheduleSpinner)
        tableLayout = findViewById(R.id.tableLayout_view)
        updateButton = findViewById(R.id.update_schedule_button)
        deleteButton = findViewById(R.id.delete_view)
        titleEditText = findViewById(R.id.title_edit_text)

        val back: ImageView = findViewById(R.id.go_back_to_home_from_view)
        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        loadSchedules()

        scheduleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSchedule = parent.getItemAtPosition(position) as Schedule
                displaySchedule(selectedSchedule)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing to do
            }
        }

        updateButton.setOnClickListener {
            updateSchedule()
        }

        deleteButton.setOnClickListener {
            deleteSchedule()
        }
    }

    private fun loadSchedules() {
        val currentUser = auth.currentUser ?: return
        firestore.collection("schedules").document(currentUser.uid).collection("userSchedules")
            .get()
            .addOnSuccessListener { result ->
                val schedules = result.map { it.toObject(Schedule::class.java).apply { id = it.id } }
                val adapter = ScheduleSpinnerAdapter(this, schedules)
                scheduleSpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar los horarios", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displaySchedule(schedule: Schedule) {
        titleEditText.setText(schedule.title)
        tableLayout.removeAllViews()
        addHeaders()
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

            for (day in daysOfWeek) {
                val cell = EditText(this)
                cell.id = View.generateViewId()
                cell.tag = "$hour-$day"
                cell.setBackgroundResource(R.drawable.cell_border)
                cell.gravity = Gravity.CENTER
                cell.setPadding(16, 16, 16, 16)
                cell.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                cell.setTextColor(ContextCompat.getColor(this, R.color.black))
                val dayText = schedule.schedule[hour]?.get(day) ?: ""
                cell.setText(dayText)
                tableRow.addView(cell)
            }

            tableLayout.addView(tableRow)
        }
    }

    private fun addHeaders() {
        val headerRow = TableRow(this)
        headerRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val emptyHeader = TextView(this)
        emptyHeader.setPadding(16, 16, 16, 16)
        headerRow.addView(emptyHeader)

        for (day in daysOfWeek) {
            val headerText = TextView(this)
            headerText.text = day
            headerText.setPadding(16, 16, 16, 16)
            headerText.setBackgroundResource(R.drawable.cell_border)
            headerText.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            headerText.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            headerText.gravity = Gravity.CENTER
            headerRow.addView(headerText)
        }

        tableLayout.addView(headerRow)
    }

    private fun updateSchedule() {
        val currentUser = auth.currentUser ?: return
        val selectedSchedule = scheduleSpinner.selectedItem as Schedule
        val updatedTitle = titleEditText.text.toString()

        val updatedSchedule = mutableMapOf<String, MutableMap<String, String>>()

        // Obtener el horario actual de Firestore
        firestore.collection("schedules").document(currentUser.uid).collection("userSchedules")
            .document(selectedSchedule.id!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val currentSchedule = document.toObject(Schedule::class.java)
                    val currentSubjects = currentSchedule?.schedule ?: mutableMapOf()

                    // Recorrer las horas del día
                    for (hour in hoursOfDay) {
                        val currentHourSubjects = currentSubjects[hour] ?: mutableMapOf()
                        val updatedHourSubjects = mutableMapOf<String, String>()

                        // Recorrer los días de la semana
                        for (day in daysOfWeek) {
                            val cell = tableLayout.findViewWithTag<EditText>("$hour-$day")
                            val subject = cell?.text?.toString() ?: ""

                            // Verificar si el sujeto ha sido cambiado
                            if (currentHourSubjects.containsKey(day) && currentHourSubjects[day] == subject) {
                                updatedHourSubjects[day] = subject
                            } else if (subject.isNotBlank()) {
                                updatedHourSubjects[day] = subject
                            }
                        }

                        // Agregar las materias actualizadas para esta hora
                        if (updatedHourSubjects.isNotEmpty()) {
                            updatedSchedule[hour] = updatedHourSubjects
                        }
                    }

                    // Crear el horario actualizado
                    val updatedScheduleObj = Schedule(selectedSchedule.id, updatedTitle, updatedSchedule)

                    // Guardar el horario actualizado en Firestore
                    firestore.collection("schedules").document(currentUser.uid).collection("userSchedules")
                        .document(selectedSchedule.id!!)
                        .set(updatedScheduleObj)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Horario actualizado", Toast.LENGTH_SHORT).show()
                            loadSchedules()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al actualizar el horario", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener el horario actual", Toast.LENGTH_SHORT).show()
            }
    }


    private fun deleteSchedule() {
        val currentUser = auth.currentUser ?: return
        val selectedSchedule = scheduleSpinner.selectedItem as Schedule

        firestore.collection("schedules").document(currentUser.uid).collection("userSchedules")
            .document(selectedSchedule.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Horario eliminado", Toast.LENGTH_SHORT).show()
                loadSchedules()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar el horario", Toast.LENGTH_SHORT).show()
            }
    }
}
