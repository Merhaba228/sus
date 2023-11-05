package com.example.sus

import SharedPrefManager
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.CalendarView
import android.graphics.Color
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.widget.ProgressBar


class timetable_activity : AppCompatActivity() {
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
        tableLayout = findViewById(R.id.timetable_tableLayout)
        calendarView = findViewById(R.id.calendarView)

        SharedPrefManager.getInstance(this).refreshDataUsingRefreshToken()

        val studentTimeTable = SharedPrefManager.getStudentTimeTable()

        createTimeTable(studentTimeTable)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            tableLayout.removeAllViews()
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate

           val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)
            loadingIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {

                val studentTimeTable: List<StudentTimeTable> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshTimeTableDateUsingRefreshToken(
                        formattedDate
                    ) { studentTimeTable ->
                        continuation.resume(studentTimeTable)
                    }
                }

                createTimeTable(studentTimeTable)
                loadingIndicator.visibility = View.GONE
            }
        }

        val button3 = findViewById<View>(R.id.arrow_back)
        button3.setOnClickListener {
            val intent = Intent(this@timetable_activity, general_activity::class.java)
            startActivity(intent)
        }
    }

    private fun createTimeTable(studentTimeTable: List<StudentTimeTable>?) {
        tableLayout.removeAllViews()
        val paddingInPixels = 20
        val paddingInDp = paddingInPixels.convertPixelsToDp(this)
        val fontSize = 14f

        fun addEmptyRow(lessonNumber: Int) {
            val emptyRow = TableRow(this)
            emptyRow.setBackgroundResource(R.drawable.table_border)

            val emptyCell = TextView(this)
            emptyCell.text = lessonNumber.toString()
            emptyCell.setTextColor(ContextCompat.getColor(this, R.color.white))
            emptyCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
            emptyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
            emptyCell.setBackgroundResource(R.drawable.timetable_table_id_cell_border)

            val emptyCellLayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            emptyCell.setLayoutParams(emptyCellLayoutParams)

            emptyRow.addView(emptyCell)

            val emptyTitleCell = TextView(this)
            emptyTitleCell.text = ""
            emptyTitleCell.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            emptyTitleCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
            emptyTitleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

            emptyRow.addView(emptyTitleCell)

            tableLayout.addView(emptyRow)
        }

        studentTimeTable?.let { timeTableList ->
            var prevGroupName = ""
            var maxLessonNumber = -1

            for (studentTimeTable in timeTableList) {
                val groupName = studentTimeTable.group
                val timeTable = studentTimeTable.timeTable


                if (groupName != prevGroupName) {
                    val separatorRow = TableRow(this)
                    val separatorCell = TextView(this)
                    separatorCell.text = ""
                    separatorCell.setPadding(0, paddingInDp * 2, 0, paddingInDp * 2)
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
                groupCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                groupCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                groupCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                val groupCellLayoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                groupCell.setLayoutParams(groupCellLayoutParams)

                groupRow.addView(groupCell)

                val facultyCell = TextView(this)
                facultyCell.text = studentTimeTable.facultyName
                facultyCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                facultyCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                facultyCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
                facultyCell.setMaxWidth(this)

                TextViewCompat.setAutoSizeTextTypeWithDefaults(
                    facultyCell,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
                )

                groupRow.setBackgroundResource(R.color.violet1)

                groupRow.addView(facultyCell)

                tableLayout.addView(groupRow)

                if (timeTable.lessons.isEmpty()) {
                    continue
                }

                if (timeTable.lessons.isNotEmpty()) {
                    val firstLessonNumber = timeTable.lessons[0].number.toInt()
                    if (firstLessonNumber > 1) {
                        for (i in 1 until firstLessonNumber) {
                            addEmptyRow(i)
                        }
                    }
                }

                for (lesson in timeTable.lessons) {
                    if (lesson.disciplines.isEmpty()) {
                        addEmptyRow(lesson.number.toInt())
                        continue
                    }

                    if (maxLessonNumber != -1 && lesson.number - maxLessonNumber > 1) {
                        for (i in maxLessonNumber + 1 until lesson.number) {
                            addEmptyRow(i)
                        }
                    }

                    for (discipline in lesson.disciplines) {
                        val lessonRow = TableRow(this)
                        lessonRow.setBackgroundResource(R.drawable.table_border)

                        val idCell = TextView(this)
                        idCell.text = lesson.number.toString() + "\n"
                        idCell.setBackgroundResource(R.drawable.timetable_table_id_cell_border)
                        idCell.setTextColor(ContextCompat.getColor(this, R.color.white))
                        idCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                        idCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                        lessonRow.addView(idCell)

                        val titleCell = TextView(this)
                        titleCell.text =
                            "${discipline.title}\n[к.${discipline.auditorium.campusId.toString()[0]} ${discipline.auditorium.number}] (${discipline.teacher.userName})"

                        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                            titleCell,
                            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
                        )

                        titleCell.setMaxWidth(this)
                        titleCell.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp)
                        titleCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)

                        lessonRow.addView(titleCell)
                        tableLayout.addView(lessonRow)
                    }
                    if (lesson.number > maxLessonNumber) {
                        maxLessonNumber = lesson.number.toInt()
                    }

                }
                val MAX_LESSON_NUMBER = studentTimeTable.timeTable.lessons.maxOfOrNull { it.number } ?: 0

                if (maxLessonNumber < MAX_LESSON_NUMBER) {
                    for (i in maxLessonNumber + 1..MAX_LESSON_NUMBER) {
                        addEmptyRow(i)
                    }

                }
                }
            }
        }
    }


    private fun Int.convertPixelsToDp(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this / scale + 0.5f).toInt()
    }

    private fun TextView.setMaxWidth(context: Context) {
        val maxWidth = (context.resources.displayMetrics.widthPixels * 0.73).toInt()
        this.maxWidth = maxWidth
    }





