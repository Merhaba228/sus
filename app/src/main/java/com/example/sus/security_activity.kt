package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity
import android.util.Log
import android.widget.CalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.widget.CalendarView.OnDateChangeListener


class security_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        SharedPrefManager.getInstance(this)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val dateTV = findViewById<TextView>(R.id.idTVDate)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

//        dateTV.text = SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Date())

        calendarView.setOnDateChangeListener(OnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate

            SharedPrefManager.refreshCalendarDateUsingRefreshToken(SharedPrefManager.getRefreshToken().toString(), formattedDate)

            val securityEvents = SharedPrefManager.getSecurityEvents()

            val tableLayout = findViewById<TableLayout>(R.id.security_tableLayout)
            tableLayout.removeAllViews()

            securityEvents?.forEach { event ->
                val tableRow = TableRow(this)
                tableRow.setBackgroundResource(R.drawable.table_border)
                tableRow.setPadding(7, 7, 5, 7)

                val timeTextView = TextView(this)
                timeTextView.text = event.time?.substring(0, 8)
                timeTextView.setTextColor(Color.BLACK)
                timeTextView.gravity = Gravity.CENTER
                timeTextView.setPadding(30, 15, 5, 15)
                timeTextView.textSize = 16f

                val buildTextView = TextView(this)
                buildTextView.text = event.build
                buildTextView.setTextColor(Color.BLACK)
                buildTextView.gravity = Gravity.START
                buildTextView.setPadding(30, 15, 5, 15)
                buildTextView.textSize = 16f

                val statusTextView = TextView(this)
                statusTextView.text = event.status
                statusTextView.setTextColor(Color.BLACK)
                statusTextView.gravity = Gravity.END
                statusTextView.textSize = 16f
                statusTextView.setPadding(80, 15, 10, 5)

                tableRow.addView(buildTextView)
                tableRow.addView(timeTextView)
                tableRow.addView(statusTextView)

                tableLayout.addView(tableRow)

                val spacerRow = TableRow(this)
                spacerRow.setPadding(0, 20, 0, 20)
                tableLayout.addView(spacerRow)
            }
        })


        val arrow_button= findViewById<View>(R.id.arrow_back)
        arrow_button.setOnClickListener {
            val intent = Intent(this@security_activity, profile_activity::class.java)
            startActivity(intent)
        }
    }
}