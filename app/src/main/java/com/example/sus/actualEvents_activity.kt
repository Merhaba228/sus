package com.example.sus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class actualEvents_activity : AppCompatActivity() {
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)

        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        tableLayout = findViewById(R.id.security_tableLayout)

        calendarView.setOnDateChangeListener(OnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate

            tableLayout.removeAllViews()

            val data = arrayListOf<String>()
            data.add("Элемент 1")
            data.add("Элемент 2")
            data.add("Элемент 3")

            for (item in data) {
                val row = TableRow(this)
                val textView = TextView(this)
                textView.text = item
                row.addView(textView)
                tableLayout.addView(row)
            }
        })

        val arrow_button= findViewById<View>(R.id.back_button)
        arrow_button.setOnClickListener {
            val intent = Intent(this@actualEvents_activity, general_activity::class.java)
            startActivity(intent)
        }
    }
}