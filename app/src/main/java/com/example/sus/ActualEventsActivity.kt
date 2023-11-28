package com.example.sus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.EventInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.util.Log
import android.widget.ImageButton

class ActualEventsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActualEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        SharedPrefManager.getInstance(this)

        val dateTV = findViewById<TextView>(R.id.idTVDate)
        recyclerView = findViewById(R.id.actualEvents_recyclerView)
        val calendarView = findViewById<CalendarView>(R.id.eventsCalendarView)
        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val currentDate = Calendar.getInstance()
        val actualEvents = SharedPrefManager.getEvents()

        adapter = ActualEventsAdapter(actualEvents)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        dateTV.text = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(Date())
        calendarView.setDate(currentDate.timeInMillis)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(calendar.time)

            val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)
            loadingIndicator.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.Main).launch {
                val actualEvents: List <EventInfo> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshEventsByDateUsingRefreshToken(formattedDate) { actualEvents ->
                        continuation.resume(actualEvents)
                    }
                }

                loadingIndicator.visibility = View.GONE
                adapter.updateEvents(actualEvents)

            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

class ActualEventsAdapter(private var events: List<EventInfo>?) : RecyclerView.Adapter<ActualEventsAdapter.ActualEventsViewHolder>() {
    inner class ActualEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.eventTitleTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.eventDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActualEventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false)
        return ActualEventsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActualEventsViewHolder, position: Int) {
        val eventCollection = events

        if (eventCollection != null && position < eventCollection.size) {
            val event = eventCollection[position]

            holder.titleTextView.text = event.name
            holder.dateTextView.text = "${event.startDate.substring(0,10)} - ${event.endDate.substring(0,10)} "
            holder.itemView.setOnClickListener{

                var eventid = event.id

                CoroutineScope(Dispatchers.Main).launch {

                    suspendCoroutine { continuation ->
                        SharedPrefManager.refreshEventUsingRefreshToken(eventid.toString()) { result ->
                            continuation.resume(result)
                        }
                    }

                    Log.d("Check_event_5","213")

                    val intent = Intent(holder.itemView.context, EventDescriptionActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }

            }

        } else {

        }

    }

    override fun getItemCount(): Int {
        return events?.size ?: 0
    }

    fun updateEvents(events: List <EventInfo>) {
        this.events = events
        notifyDataSetChanged()
    }
}

