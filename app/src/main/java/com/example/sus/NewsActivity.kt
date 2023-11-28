package com.example.sus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class NewsActivity : AppCompatActivity() {

    private lateinit var newsAdapter: NewsAdapter
    private var currentPage: Int = 1
    private val pageSize: Int = 10
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val recyclerView: RecyclerView = findViewById(R.id.news_recyclerView)
        progressBar = findViewById(R.id.loadingIndicator)
        newsAdapter = NewsAdapter()

        recyclerView.adapter = newsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnPreviousPage).setOnClickListener {
            loadPreviousPage()
        }

        findViewById<Button>(R.id.btnNextPage).setOnClickListener {
            loadNextPage()
        }

        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        loadNewsData(currentPage)
    }

    private fun loadNewsData(page: Int) {
        try {
            val newsList = SharedPrefManager.getInstance(this).getNewsList()
            if (newsList == null || newsList.isEmpty()) {
                findViewById<TextView>(R.id.errorTextView).visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                return
            }

            val startIndex = (page - 1) * pageSize
            val endIndex = startIndex + pageSize
            val currentNewsPage = newsList.subList(startIndex, minOf(endIndex, newsList.size))

            GlobalScope.launch(Dispatchers.Main) {
                newsAdapter.updateNewsList(currentNewsPage)
                progressBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e("NewsActivity", "Error loading news data: ${e.message}")
            findViewById<TextView>(R.id.errorTextView).text = "Ошибка загрузки новостей"
            findViewById<TextView>(R.id.errorTextView).visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun loadNextPage() {
        currentPage++
        loadNewsData(currentPage)

        val recyclerView: RecyclerView = findViewById(R.id.news_recyclerView)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }

    private fun loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            loadNewsData(currentPage)
        }

        val recyclerView: RecyclerView = findViewById(R.id.news_recyclerView)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<News> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateNewsList(newNewsList: List<News>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val webViewContent: WebView = itemView.findViewById(R.id.webViewContent)

        fun bind(news: News) {
            titleTextView.text = news.date?.substring(0, minOf(news.date.length, 10))
            webViewContent.loadDataWithBaseURL(null, news.text, "text/html", "UTF-8", null)
        }
    }
}
