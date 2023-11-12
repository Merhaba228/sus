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
interface OnItemClickListener {
    fun onItemClick(disciplineId: String)
}

class DisciplinesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordBooksAdapter: RecordBooksAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplines)

        SharedPrefManager.getInstance(this)

        recyclerView = findViewById<RecyclerView>(R.id.disciplines_recyclerView)
        progressBar = findViewById(R.id.loadingIndicator)

        val studentSemester = SharedPrefManager.getStudentSemester()
        recyclerView = findViewById<RecyclerView>(R.id.disciplines_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recordBooksAdapter = RecordBooksAdapter(studentSemester?.recordBooks, object : OnItemClickListener {
            override fun onItemClick(disciplineId: String) {

                progressBar.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.Main).launch {
                    suspendCoroutine { continuation ->
                        SharedPrefManager.refreshCurrentDisciplineUsingRefreshToken(disciplineId) { result ->
                            continuation.resume(result)
                        }
                    }

                    suspendCoroutine { continuation ->
                        SharedPrefManager.refreshStudentRatingPlanUsingRefreshToken(disciplineId) { result ->
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
