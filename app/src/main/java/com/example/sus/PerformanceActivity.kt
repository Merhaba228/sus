package com.example.sus

import SharedPrefManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.*
import android.widget.TextView
import com.example.sus.R
import java.text.SimpleDateFormat
import android.widget.ProgressBar
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import java.util.Locale
import kotlin.math.round

class PerformanceActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var controlDotsAdapter: ControlDotsAdapter
    private lateinit var totalBallsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance)
        SharedPrefManager.getInstance(this)

        titleTextView = findViewById(R.id.textView_performance_discipline)
        totalBallsTextView = findViewById(R.id.textViewTotalBalls)
        recyclerView = findViewById(R.id.performance_discipline_recyclerView)
        titleTextView.text = SharedPrefManager.getDiscipline()?.title
        val zeroSessionTextView: TextView = findViewById(R.id.textViewZeroSession)
        if (SharedPrefManager.getStudentRatingPlan().markZeroSession != null) {
            zeroSessionTextView.text = "Нулевая сессия: ${SharedPrefManager.getStudentRatingPlan().markZeroSession.ball} / 5.0"
        } else {
            zeroSessionTextView.text = "Нулевая сессия: (балл не указан)"
        }

        val sections: List<Sections> = SharedPrefManager.getStudentRatingPlan().sections

        var totalMarks = 0.0
        var maxMarks = 0.0
        for (section in sections) {
            for (controlDot in section.controlDots) {
                controlDot.maxBall?.let{
                    maxMarks += it
                }
                controlDot.mark?.ball?.let {
                    totalMarks += it
                }
            }
        }
        totalBallsTextView.text = "Итого: ${round(totalMarks)} / ${maxMarks} "

        controlDotsAdapter = ControlDotsAdapter(sections)
        recyclerView.adapter = controlDotsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

class ControlDotsAdapter(private val sectionsList: List<Sections>) :
    RecyclerView.Adapter<ControlDotsAdapter.ControlDotViewHolder>() {

    inner class ControlDotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitleTextView: TextView = itemView.findViewById(R.id.textViewSectionTitle)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewControlDots)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlDotViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_performance, parent, false)
        return ControlDotViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ControlDotViewHolder, position: Int) {
        val currentSections = sectionsList[position]

        if (currentSections.title != null) {
            holder.sectionTitleTextView.text = currentSections.title
        } else {
            holder.sectionTitleTextView.text = "(Название контрольной точки не указано)"
        }

        holder.sectionTitleTextView.text = currentSections.title

        val controlDotsList = currentSections.controlDots
        val controlDotsAdapter = InnerControlDotsAdapter(controlDotsList)
        holder.recyclerView.adapter = controlDotsAdapter
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.recyclerView.context)
    }

    override fun getItemCount(): Int {
        return sectionsList.size
    }

    inner class InnerControlDotsAdapter(private val controlDotsList: List<ControlDots>) :
        RecyclerView.Adapter<InnerControlDotsAdapter.InnerControlDotViewHolder>() {

        inner class InnerControlDotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.textViewControlDotTitle)
            val dateTextView: TextView = itemView.findViewById(R.id.textViewControlDotDate)
            val markTextView: TextView = itemView.findViewById(R.id.textViewControlDotMark)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerControlDotViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_control_dot, parent, false)
            return InnerControlDotViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: InnerControlDotViewHolder, position: Int) {
            val currentControlDot = controlDotsList[position]

            holder.titleTextView.text = currentControlDot.title

            if (currentControlDot.date.isNullOrEmpty()) {
                holder.dateTextView.text = "Срок сдачи: не установлен"
            } else {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = inputFormat.parse(currentControlDot.date)

                val outputFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
                val formattedDate = outputFormat.format(date)

                holder.dateTextView.text = "Срок сдачи: $formattedDate"
            }

            if (currentControlDot.mark != null && currentControlDot.maxBall != null) {
                holder.markTextView.text = "Балл: ${currentControlDot.mark.ball} / ${currentControlDot.maxBall}"
            } else {
                holder.markTextView.text = "Балл: ? / ${currentControlDot.maxBall}"
            }
        }

        override fun getItemCount(): Int {
            return controlDotsList.size
        }
    }
}