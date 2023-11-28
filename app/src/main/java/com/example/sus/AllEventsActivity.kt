package com.example.sus

import SharedPrefManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sus.activity.logic.auth.retrofit.dto.EventInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.util.Log
import android.widget.ImageButton


class AllEventsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)
        SharedPrefManager.getInstance(this)

        recyclerView = findViewById(R.id.allEvents_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.Main).launch {
            val result = suspendCoroutine { continuation ->
                SharedPrefManager.refreshAllEventUsingRefreshToken() { result ->
                    continuation.resume(result)
                }
            }
            adapter = AllEventsAdapter(result)
            recyclerView.adapter = adapter
        }

        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

class AllEventsAdapter(private var events: List<EventInfo>?) : RecyclerView.Adapter<AllEventsAdapter.AllEventsViewHolder>() {
    inner class AllEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.eventTitleTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.eventDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllEventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false)
        return AllEventsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllEventsViewHolder, position: Int) {
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


}

