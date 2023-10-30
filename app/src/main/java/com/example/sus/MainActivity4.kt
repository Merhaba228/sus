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

class MainActivity4 : AppCompatActivity() {
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)

        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        tableLayout = findViewById(R.id.tableLayout)

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

        val button3 = findViewById<View>(R.id.back_button)
        button3.setOnClickListener {
            val intent = Intent(this@MainActivity4, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}