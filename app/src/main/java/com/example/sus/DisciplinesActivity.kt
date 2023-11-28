package com.example.sus

import SharedPrefManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.ImageButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
interface OnItemClickListener {
    fun onItemClick(disciplineId: String)
}

class DisciplinesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordBooksAdapter: RecordBooksAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var yearSpinner: Spinner
    private lateinit var semesterSpinner: Spinner
    private var isNotFirstEntering: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplines)
        isNotFirstEntering = true
        SharedPrefManager.getInstance(this)

        recyclerView = findViewById(R.id.disciplines_recyclerView)
        progressBar = findViewById(R.id.loadingIndicator)
        yearSpinner = findViewById(R.id.yearSpinner)
        semesterSpinner = findViewById(R.id.semesterSpinner)

        val years = arrayOf("2023 - 2024", "2022 - 2023", "2021 - 2022")
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        val semesters = arrayOf("1", "2")
        val semesterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        semesterSpinner.adapter = semesterAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        updateRecyclerView()
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                updateRecyclerView()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        semesterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                updateRecyclerView()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    internal fun updateRecyclerView() {
        val selectedYear = yearSpinner.selectedItem.toString()
        val selectedSemester = semesterSpinner.selectedItem.toString()

        if(!isNotFirstEntering) {
            runBlocking {
                launch {
                    SharedPrefManager.refreshStudentSemesterByDateUsingRefreshToken(
                        selectedYear,
                        selectedSemester.toInt()
                    )
                }

                delay(400)
            }
        }

        isNotFirstEntering = false

        recordBooksAdapter = RecordBooksAdapter(SharedPrefManager.getStudentSemester()?.recordBooks, object : OnItemClickListener {
                override fun onItemClick(disciplineId: String) {
                    progressBar.visibility = View.VISIBLE

                    CoroutineScope(Dispatchers.Main).launch {
                        suspendCoroutine { continuation ->
                            SharedPrefManager.refreshStudentRatingPlanUsingRefreshToken(disciplineId) { result ->
                                continuation.resume(result)
                            }
                        }

                        suspendCoroutine { continuation ->
                            SharedPrefManager.refreshCurrentDisciplineUsingRefreshToken(disciplineId) { result ->
                                continuation.resume(result)
                            }
                        }

                        progressBar.visibility = View.GONE
                        val intent = Intent(this@DisciplinesActivity, PerformanceActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
            recyclerView.adapter = recordBooksAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

class RecordBooksAdapter(
    private var recordBookList: List<RecordBooks_StudentSemester>?,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecordBooksAdapter.RecordBooksViewHolder>() {
    inner class RecordBooksViewHolder(itemView: View, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val recordBookTitleTextView: TextView = itemView.findViewById(R.id.recordBookTitle_textView)
        val disciplinesRecyclerView: RecyclerView = itemView.findViewById(R.id.disciplinesRecyclerView)

        fun bind(recordBook: RecordBooks_StudentSemester?) {
            recordBookTitleTextView.text = recordBook?.faculty
            val disciplinesAdapter = DisciplinesAdapter(recordBook?.discipline, itemClickListener)
            disciplinesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            disciplinesRecyclerView.adapter = disciplinesAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBooksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_record_books, parent, false)
        return RecordBooksViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecordBooksViewHolder, position: Int) {
        val recordBook = recordBookList?.getOrNull(position)
        holder.bind(recordBook)
    }

    override fun getItemCount(): Int {
        return recordBookList?.size ?: 0
    }
}

class DisciplinesAdapter(
    private var disciplineList: List<Discipline>?,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DisciplinesAdapter.DisciplinesViewHolder>() {
    inner class DisciplinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val disciplineNameTextView: TextView = itemView.findViewById(R.id.disciplineName_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisciplinesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_disciplines, parent, false)
        return DisciplinesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisciplinesViewHolder, position: Int) {
        val discipline = disciplineList?.getOrNull(position)
        holder.disciplineNameTextView.text = discipline?.title

        // Установка слушателя на элемент
        holder.itemView.setOnClickListener {
            discipline?.id?.let { id -> itemClickListener.onItemClick(id.toString()) }
        }
    }

    override fun getItemCount(): Int {
        return disciplineList?.size ?: 0
    }
}
