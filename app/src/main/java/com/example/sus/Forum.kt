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

class Forum : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordBooksAdapter: RecordBooksAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var yearSpinner: Spinner
    private lateinit var semesterSpinner: Spinner
    private var isNotFirstEntering: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)
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
                    val intent = Intent(this@Forum, ForumMessage::class.java)
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

