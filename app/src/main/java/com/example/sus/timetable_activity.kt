package com.example.sus

import SharedPrefManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import android.text.SpannableStringBuilder
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
import android.widget.CalendarView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.loginapp.activity.logic.auth.retrofit.dto.UserPhoto
import com.example.sus.activity.logic.auth.retrofit.dto.Schedule
import com.example.sus.activity.logic.auth.retrofit.dto.TimeTableLesson
import com.example.sus.activity.logic.auth.retrofit.dto.TimeTableLessonDiscipline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import androidx.core.text.HtmlCompat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TimeTableActivity : AppCompatActivity()
{
    private lateinit var dateTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var timeTableAdapter: TimeTableAdapter
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)


        calendarView = findViewById(R.id.calendarView)
        dateTextView = findViewById(R.id.idTVDate)
        recyclerView = findViewById(R.id.timetable_recyclerView)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)

        recyclerView.layoutManager = LinearLayoutManager(this)
        timeTableAdapter = TimeTableAdapter()
        recyclerView.adapter = timeTableAdapter

        SharedPrefManager.getInstance(this).refreshDataUsingRefreshToken()

        val studentTimeTable = SharedPrefManager.getStudentTimeTable()
        dateTextView.text = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(Date())
        createTimeTable(studentTimeTable)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTextView.text = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(calendar.time)

            loadingIndicator.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.Main).launch {
                val studentTimeTable: List<StudentTimeTable> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshTimeTableDateUsingRefreshToken(formattedDate) { result ->
                        continuation.resume(result)
                    }
                }

                createTimeTable(studentTimeTable)
                loadingIndicator.visibility = View.GONE
            }
        }

        val button3 = findViewById<View>(R.id.arrow_back)
        button3.setOnClickListener {
            val intent = Intent(this@TimeTableActivity, bottom_menu::class.java)
            intent.putExtra("activityName", "general_activity")
            startActivity(intent)
        }
    }

    private fun createTimeTable(studentTimeTable: List<StudentTimeTable>?) {
        studentTimeTable?.let { timeTableList ->
            timeTableAdapter.submitList(timeTableList)
        }
    }

    inner class TimeTableAdapter : RecyclerView.Adapter<TimeTableAdapter.TimeTableViewHolder>() {
        private var timeTableList: List<StudentTimeTable>? = null

        fun submitList(timeTableList: List<StudentTimeTable>) {
            this.timeTableList = timeTableList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_time_table, parent, false)
            return TimeTableViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int) {
            val studentTimeTable = timeTableList?.get(position)
            studentTimeTable?.let {
                holder.bind(it)
            }
        }

        override fun getItemCount(): Int {
            return timeTableList?.size ?: 0
        }

        inner class TimeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
            private val lessonsRecyclerView: RecyclerView = itemView.findViewById(R.id.lessonsRecyclerView)

            fun bind(studentTimeTable: StudentTimeTable) {
                groupNameTextView.text = studentTimeTable.facultyName + " (" + studentTimeTable.group + " г.)"

                lessonsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                val lessonAdapter = LessonAdapter()
                lessonsRecyclerView.adapter = lessonAdapter
                val lessonDisciplines = mutableListOf<Pair<TimeTableLessonDiscipline?, Int>>()
                val maxSecond = studentTimeTable.timeTable.lessons.map { it.number.toInt() }.maxOrNull() ?: 0

                for (i in 1..maxSecond) {
                    val found = studentTimeTable.timeTable.lessons.find { it.number.toInt() == i }
                    if (found == null) {
                        lessonDisciplines.add(Pair(null, i))
                    } else {
                        for (discipline in found.disciplines) {
                            lessonDisciplines.add(discipline to i)
                        }
                    }
                }

                lessonAdapter.submitList(lessonDisciplines.toList())
            }
        }

    }

    inner class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
        private var lessonList: List<Pair<TimeTableLessonDiscipline?, Int>> = listOf()

        fun submitList(lessonList: List<Pair<TimeTableLessonDiscipline?, Int>>) {
            this.lessonList = lessonList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
            return LessonViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
            val lesson = lessonList?.get(position)
            lesson?.let {
                holder.bind(it)
            }
        }

        override fun getItemCount(): Int {
            return lessonList?.size ?: 0
        }

        inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val lessonNameTextView: TextView = itemView.findViewById(R.id.lessonNameTextView)
            private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)

            fun bind(lessonPair: Pair<TimeTableLessonDiscipline?, Int>) {
                val lesson = lessonPair.first
                val lessonNumber = lessonPair.second

                locationTextView.text = " ${lessonNumber}. ${Schedule.getByNumber(lessonNumber)?.time}"
                val spannableBuilder = SpannableStringBuilder()
                if (lesson?.title?.isNotBlank() == true) {
                    val formattedTitle = SpannableString(lesson.title)
                    formattedTitle.setSpan(StyleSpan(Typeface.BOLD), 0, formattedTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableBuilder.append(formattedTitle)
                    spannableBuilder.append("\n")
                    spannableBuilder.append("[к.${lesson?.auditorium?.campusId?.toString()?.get(0)} ${lesson?.auditorium?.number}] (${lesson?.teacher?.userName})")
                    lessonNameTextView.text = spannableBuilder

                    val teacherPhoto: ImageView = itemView.findViewById(R.id.teacher_image)
                    val profilePhotoUrl = lesson?.teacher?.photo?.urlSmall
                    Glide.with(itemView.context)
                        .load(profilePhotoUrl)
                        .placeholder(R.drawable.cot_profile)
                        .transform(RoundedCorners(100))
                        .into(teacherPhoto)
                }
                    else lessonNameTextView.text = "(Пара отсутствует)"

            }
        }
    }
}


