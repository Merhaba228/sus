package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class general_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        SharedPrefManager.getInstance(this).refreshDataUsingRefreshToken()

        val button = findViewById<View>(R.id.exit_button)
        button.setOnClickListener()
        {
            SharedPrefManager.clearData()
            val intent = Intent(this@general_activity, login_activity::class.java)
            startActivity(intent)
        }

//        val button2 = findViewById<View>(R.id.profile_button)
//        button2.setOnClickListener()
//        {
//            val intent = Intent(this@general_activity, profile_activity::class.java)
//            startActivity(intent)
//        }

//        val button3 = findViewById<View>(R.id.timetable_button)
//        button3.setOnClickListener()
//        {
//            val intent = Intent(this@general_activity, TimeTableActivity::class.java)
//            startActivity(intent)
//        }

        val button4 = findViewById<View>(R.id.events_button)
        button4.setOnClickListener()
        {
            val intent = Intent(this@general_activity, ActualEventsActivity::class.java)
            startActivity(intent)
        }

        val button5 = findViewById<View>(R.id.polls_button)
        button5.setOnClickListener()
        {
            val intent = Intent(this@general_activity, polls_activity::class.java)
            startActivity(intent)
        }
    }
}