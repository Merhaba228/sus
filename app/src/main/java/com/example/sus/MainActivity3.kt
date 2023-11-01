package com.example.sus

import SharedPrefManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val emailTextView: TextView = findViewById(R.id.textView9)
        val studentIDTextView: TextView = findViewById(R.id.textView10)
        val button = findViewById<View>(R.id.back_button)

        emailTextView.text = SharedPrefManager.getInstance(this).getUserData()?.email
        studentIDTextView.text = "ID: ${SharedPrefManager.getInstance(this).getUserData()?.studentCod}"

        button.setOnClickListener {
            val intent = Intent(this@MainActivity3, MainActivity2::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<View>(R.id.exitButton2)
        button2.setOnClickListener {
            val intent = Intent(this@MainActivity3, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
