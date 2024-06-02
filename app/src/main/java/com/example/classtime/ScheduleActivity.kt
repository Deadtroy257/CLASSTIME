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

class ScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        // Configurar el botón para retroceder
        val back: ImageView = findViewById(R.id.go_back_to_home)
        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val addButton = findViewById<Button>(R.id.add_schedule_button)
        val daysOfWeek = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val hoursOfDay = arrayOf("8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 1:00")

        // Añadir encabezados de columna
        val headerRow = TableRow(this)
        headerRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        // Esquina superior izquierda vacía
        val emptyHeader = TextView(this)
        emptyHeader.setPadding(16, 16, 16, 16)
        emptyHeader.setBackgroundResource(R.drawable.cell_border)
        headerRow.addView(emptyHeader)

        for (day in daysOfWeek) {
            val header = TextView(this)
            header.text = day
            header.gravity = Gravity.CENTER
            header.setPadding(16, 16, 16, 16)
            header.setBackgroundResource(R.drawable.cell_border)
            header.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            header.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            headerRow.addView(header)
        }
        tableLayout.addView(headerRow)

        // Añadir filas para cada hora
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

        addButton.setOnClickListener {
            //saveSchedule(tableLayout)
        }

    }

    private fun saveSchedule(tableLayout: TableLayout) {
        val scheduleData = mutableMapOf<String, String>()

        for (i in 1 until tableLayout.childCount) { // Saltar la fila de encabezado
            val row = tableLayout.getChildAt(i) as TableRow
            val hourText = (row.getChildAt(0) as TextView).text.toString()

            for (j in 1 until row.childCount) {
                val cell = row.getChildAt(j) as EditText
                val dayIndex = j - 1
                val day = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")[dayIndex]
                val cellTag = "$hourText-$day"
                val cellText = cell.text.toString()
                if (cellText.isNotEmpty()) {
                    scheduleData[cellTag] = cellText
                }
            }
        }


    }

}
