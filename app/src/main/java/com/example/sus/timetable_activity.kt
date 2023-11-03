package com.example.sus

import SharedPrefManager
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.CalendarView
import android.graphics.Color
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.text.TextUtils

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
        val studentTimeTable = SharedPrefManager.getStudentTimeTable()

        populateTimeTable(studentTimeTable)

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

    private fun populateTimeTable(studentTimeTable: List<StudentTimeTable>?) {
        tableLayout.removeAllViews()

        studentTimeTable?.let { timeTableList ->
            var prevGroupName = ""
            var prevLessonNumber = -1
            var lessonIndex = 1

            for (studentTimeTable in timeTableList) {
                val groupName = studentTimeTable.group
                val timeTable = studentTimeTable.timeTable

                if (groupName != prevGroupName) {
                    val separatorRow = TableRow(this)
                    val separatorCell = TextView(this)
                    separatorCell.text = ""
                    separatorCell.setPadding(0, 10, 0, 10)
                    separatorRow.addView(separatorCell)
                    tableLayout.addView(separatorRow)
                    prevGroupName = groupName
                }

                val groupRow = TableRow(this)
                groupRow.setBackgroundColor(Color.WHITE)
                val groupLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                groupLayoutParams.setMargins(7, 7, 7, 7)
                groupRow.layoutParams = groupLayoutParams

                val groupCell = TextView(this)
                groupCell.text = " (${groupName}) \n"
                groupCell.setTextColor(ContextCompat.getColor(this, R.color.black))
                groupCell.setPadding(5, 5, 5, 5)
                groupCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                groupCell.setBackgroundResource(R.color.white)

                val groupCellLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                groupCell.setLayoutParams(groupCellLayoutParams)

                groupRow.addView(groupCell)

                val facultyCell = TextView(this)
                facultyCell.text = studentTimeTable.facultyName
                facultyCell.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                facultyCell.setPadding(5, 5, 5, 5)
                facultyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                facultyCell.setSingleLine(false)
                facultyCell.setEllipsize(null)

                TextViewCompat.setAutoSizeTextTypeWithDefaults(facultyCell, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)

                groupRow.addView(facultyCell)


                tableLayout.addView(groupRow)

                for (lesson in timeTable.lessons) {
                    if (lesson.disciplines.isEmpty()) {
                        val emptyRow = TableRow(this)
                        emptyRow.setBackgroundResource(R.drawable.table_border)

                        val emptyCell = TextView(this)
                        emptyCell.text = lesson.number.toString()
                        emptyCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                        emptyCell.setPadding(5, 5, 5, 5)
                        emptyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                        emptyCell.setBackgroundResource(R.color.violet1)

                        val emptyCellLayoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        emptyCell.setLayoutParams(emptyCellLayoutParams)

                        emptyRow.addView(emptyCell)

                        val emptyTitleCell = TextView(this)
                        emptyTitleCell.setSingleLine(false)
                        emptyTitleCell.setEllipsize(TextUtils.TruncateAt.END)
                        emptyTitleCell.text = ""
                        emptyTitleCell.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        emptyTitleCell.setPadding(5, 5, 5, 5)
                        emptyTitleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

                        emptyRow.addView(emptyTitleCell)

                        tableLayout.addView(emptyRow)
                        continue
                    }

                    for (discipline in lesson.disciplines) {
                        val lessonRow = TableRow(this)
                        lessonRow.setBackgroundResource(R.drawable.table_border)

                        if (prevLessonNumber != -1 && lesson.number - prevLessonNumber > 1) {
                            // Add empty rows for intermediate lesson numbers
                            for (i in prevLessonNumber + 1 until lesson.number) {
                                val emptyRow = TableRow(this)
                                emptyRow.setBackgroundResource(R.drawable.table_border)

                                val emptyCell = TextView(this)
                                emptyCell.text = i.toString()
                                emptyCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                                emptyCell.setPadding(5, 5, 5,                            5)
                                emptyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                                emptyCell.setBackgroundResource(R.color.violet1)

                                val emptyCellLayoutParams = TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                                )
                                emptyCell.setLayoutParams(emptyCellLayoutParams)

                                emptyRow.addView(emptyCell)

                                val emptyTitleCell = TextView(this)
                                emptyTitleCell.setSingleLine(false)
                                emptyTitleCell.setEllipsize(TextUtils.TruncateAt.END)
                                emptyTitleCell.text = ""
                                emptyTitleCell.layoutParams = TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                                )
                                emptyTitleCell.setPadding(5, 5, 5, 5)
                                emptyTitleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

                                emptyRow.addView(emptyTitleCell)

                                tableLayout.addView(emptyRow)
                            }
                        }

                        val idCell = TextView(this)
                        idCell.text = "${lesson.number.toString()}\n"
                        idCell.setBackgroundResource(R.drawable.table_border)
                        idCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                        idCell.setPadding(5, 5, 5, 5)
                        idCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                        idCell.setBackgroundResource(R.color.violet1)

                        val idCellLayoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        idCell.setLayoutParams(idCellLayoutParams)

                        lessonRow.addView(idCell)
                        lessonIndex++

                        val titleCell = TextView(this)
                        titleCell.setSingleLine(false)
                        titleCell.setEllipsize(TextUtils.TruncateAt.END)
                        titleCell.text = "${discipline.title}\n[к.${discipline.auditorium.campusId.toString()[0]} ${discipline.auditorium.number}] (${discipline.teacher.fio})"
                        titleCell.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        titleCell.setPadding(5, 5, 5, 5)
                        titleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

                        lessonRow.addView(titleCell)

                        tableLayout.addView(lessonRow)

                        prevLessonNumber = lesson.number.toInt()
                    }
                }
            }
        }
    }







}
