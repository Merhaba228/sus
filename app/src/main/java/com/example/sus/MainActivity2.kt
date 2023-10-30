package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val button = findViewById<View>(R.id.back_button)
        button.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<View>(R.id.imageView)
        button2.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity3::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<View>(R.id.timetableButton)
        button3.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity4::class.java)
            startActivity(intent)
        }

        val button4 = findViewById<View>(R.id.events_Button)
        button4.setOnClickListener()
        {
            val intent = Intent(this@MainActivity2, MainActivity5::class.java)
            startActivity(intent)
        }
    }
}