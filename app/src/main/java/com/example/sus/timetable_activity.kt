package com.example.sus

import SharedPrefManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class timetable_activity : AppCompatActivity() {
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var tableLayout: TableLayout

    @SuppressLint("MissingInflatedId")
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
        tableLayout = findViewById(R.id.timetable_tableLayout)
        SharedPrefManager.getInstance(this)

//        calendarView.setOnDateChangeListener(OnDateChangeListener { _, year, month, dayOfMonth ->
//            val calendar = Calendar.getInstance()
//            calendar.set(year, month, dayOfMonth)
//
//            val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
//            val formattedDate = dateFormat.format(calendar.time)
//            dateTV.text = formattedDate
//
//            val studentTimeTable = SharedPrefManager.getStudentTimeTable()
//
//            populateTimeTable(studentTimeTable)
//        })

        SharedPrefManager.getInstance(this)


        val button3 = findViewById<View>(R.id.back_button)
        button3.setOnClickListener {
            val intent = Intent(this@timetable_activity, general_activity::class.java)
            startActivity(intent)
        }
    }

    private fun populateTimeTable(studentTimeTable: kotlin.collections.List<StudentTimeTable>?) {
        tableLayout.removeAllViews()

        studentTimeTable?.let { timeTableList ->
            for (studentTimeTable in timeTableList) {
                val groupName = studentTimeTable.group
                val timeTable = studentTimeTable.timeTable

                val groupRow = TableRow(this)
                val groupCell = TextView(this)
                groupCell.text = groupName
                groupRow.addView(groupCell)
                tableLayout.addView(groupRow)

                for (lesson in timeTable.lessons) {
                    for (discipline in lesson.disciplines) {
                        val lessonRow = TableRow(this)

                        val idCell = TextView(this)
                        idCell.text = discipline.id.toString()
                        lessonRow.addView(idCell)

                        val titleCell = TextView(this)
                        titleCell.text = discipline.title
                        lessonRow.addView(titleCell)

                        val teacherCell = TextView(this)
                        teacherCell.text = discipline.teacher.fio
                        lessonRow.addView(teacherCell)

                        tableLayout.addView(lessonRow)
                    }
                }
            }
        }
    }




}
