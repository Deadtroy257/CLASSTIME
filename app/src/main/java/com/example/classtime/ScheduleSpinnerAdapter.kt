package com.example.classtime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.ImageView

class ScheduleSpinnerAdapter(context: Context, private val schedules: List<Schedule>) :
    ArrayAdapter<Schedule>(context, 0, schedules) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)

        val schedule = getItem(position)
        val textView = view.findViewById<TextView>(R.id.text_view)
        textView.text = schedule?.title
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))

        val iconView = view.findViewById<ImageView>(R.id.icon_view)
        iconView.visibility = View.VISIBLE // Asegúrate de que el icono sea visible en el Spinner principal
        iconView.setImageResource(R.drawable.ic_arrow_down)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false) // Cambiado a diseño predeterminado
        val schedule = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1) // Cambiado a id predeterminado
        textView.text = schedule?.title
        textView.setTextColor(ContextCompat.getColor(context, R.color.black)) // Cambiado a color de texto negro para las opciones desplegables
        return view
    }
}
