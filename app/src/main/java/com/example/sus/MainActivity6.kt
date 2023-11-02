package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val arrow_button= findViewById<View>(R.id.arrow_back)
        arrow_button.setOnClickListener {
            val intent = Intent(this@MainActivity6, MainActivity3::class.java)
            startActivity(intent)
        }
    }
}