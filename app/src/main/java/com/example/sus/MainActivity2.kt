package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        SharedPrefManager.getInstance(this).refreshDataUsingRefreshToken(SharedPrefManager.getRefreshToken().toString())

        val button = findViewById<View>(R.id.back_button)
        button.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<View>(R.id.profile_button)
        button2.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity3::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<View>(R.id.timetable_button)
        button3.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity4::class.java)
            startActivity(intent)
        }

        val button4 = findViewById<View>(R.id.events_button)
        button4.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity5::class.java)
            startActivity(intent)
        }

        val button5 = findViewById<View>(R.id.polls_button)
        button5.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity7::class.java)
            startActivity(intent)
        }
    }
}