package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.loginapp.activity.logic.auth.retrofit.dto.SecurityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.ViewGroup
import android.view.LayoutInflater

class security_activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SecurityEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val locale = Locale("ru")
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        SharedPrefManager.getInstance(this)

        val dateTV = findViewById<TextView>(R.id.idTVDate)
        recyclerView = findViewById<RecyclerView>(R.id.security_recyclerView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        val currentDate = Calendar.getInstance()
        val securityEvents = SharedPrefManager.getSecurityEvents()

        adapter = SecurityEventAdapter(securityEvents)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        dateTV.text = SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Date())
        calendarView.setDate(currentDate.timeInMillis)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            dateTV.text = formattedDate

            val loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)
            loadingIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {

                val securityEvents: List<SecurityEvent> = suspendCoroutine { continuation ->
                    SharedPrefManager.refreshCalendarDateUsingRefreshToken(formattedDate) { securityEvents ->
                        continuation.resume(securityEvents)
                    }
                }

                loadingIndicator.visibility = View.GONE
                adapter.updateSecurityEvents(securityEvents)

            }
        }

        val arrow_button = findViewById<View>(R.id.arrow_back)
        arrow_button.setOnClickListener {
            val intent = Intent(this@security_activity, profile_activity::class.java)
            startActivity(intent)
        }
    }
}

class SecurityEventAdapter(private var securityEvents: List<SecurityEvent>?) : RecyclerView.Adapter<SecurityEventAdapter.SecurityEventViewHolder>() {
    inner class SecurityEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val buildTextView: TextView = itemView.findViewById(R.id.buildTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityEventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.security_event_item, parent, false)
        return SecurityEventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SecurityEventViewHolder, position: Int) {
        val event = securityEvents!![position]

        holder.timeTextView.text = event.time?.substring(0, 8)
        holder.buildTextView.text = event.build
        holder.statusTextView.text = event.status
    }

    override fun getItemCount(): Int {
        Log.d("size_recycler",securityEvents!!.size.toString() )
        return securityEvents!!.size
    }

    fun updateSecurityEvents(events: List<SecurityEvent>) {
        securityEvents = events
        notifyDataSetChanged()
    }
}
