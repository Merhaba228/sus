package com.example.sus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.activity.logic.auth.retrofit.dto.*
import com.example.sus.activity.logic.auth.retrofit.dto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class disciplines_activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordBooksAdapter: RecordBooksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplines)

        SharedPrefManager.getInstance(this)

        recyclerView = findViewById<RecyclerView>(R.id.disciplines_recyclerView)

        val studentSemester = SharedPrefManager.getStudentSemester()
        recyclerView = findViewById<RecyclerView>(R.id.disciplines_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recordBooksAdapter = RecordBooksAdapter(studentSemester?.recordBooks)
        recyclerView.adapter = recordBooksAdapter
    }
}

class RecordBooksAdapter(private var recordBookList: List<RecordBooks_StudentSemester>?) : RecyclerView.Adapter<RecordBooksAdapter.RecordBooksViewHolder>() {

    inner class RecordBooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recordBookTitleTextView: TextView = itemView.findViewById(R.id.recordBookTitle_textView)
        val disciplinesRecyclerView: RecyclerView = itemView.findViewById(R.id.disciplinesRecyclerView)

        fun bind(recordBook: RecordBooks_StudentSemester?) {
            recordBookTitleTextView.text = recordBook?.faculty
            val disciplinesAdapter = DisciplinesAdapter(recordBook?.discipline)
            disciplinesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            disciplinesRecyclerView.adapter = disciplinesAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBooksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_record_books, parent, false)
        return RecordBooksViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordBooksViewHolder, position: Int) {
        val recordBook = recordBookList?.getOrNull(position)
        holder.bind(recordBook)
    }

    override fun getItemCount(): Int {
        return recordBookList?.size ?: 0
    }
}

class DisciplinesAdapter(private var disciplineList: List<Discipline>?) : RecyclerView.Adapter<DisciplinesAdapter.DisciplinesViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return disciplineList?.size ?: 0
    }
}
