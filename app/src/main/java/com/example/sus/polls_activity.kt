package com.example.sus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class polls_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        val arrow_button= findViewById<View>(R.id.arrow_back)
        arrow_button.setOnClickListener {
            val intent = Intent(this@polls_activity, general_activity::class.java)
            startActivity(intent)
        }
    }
}