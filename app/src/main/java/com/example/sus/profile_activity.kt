package com.example.sus

import SharedPrefManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class profile_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        SharedPrefManager.getInstance(this).refreshDataUsingRefreshToken()

        val emailTextView: TextView = findViewById(R.id.textView9)
        val studentIDTextView: TextView = findViewById(R.id.textView10)
        val profilePictureImageView: ImageView = findViewById(R.id.imageView_profile)

        emailTextView.text = SharedPrefManager.getUserData()?.email
        studentIDTextView.text = "ID: ${SharedPrefManager.getUserData()?.studentCod}"

        val profilePhotoUrl = SharedPrefManager.getUserData()?.photo?.urlMedium

        Glide.with(this)
            .load(profilePhotoUrl)
            .transform(CircleCrop())
            .placeholder(R.drawable.cot_profile)
            .into(profilePictureImageView)


        val button = findViewById<View>(R.id.back_button)
        button.setOnClickListener {
            val intent = Intent(this@profile_activity, general_activity::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<View>(R.id.exitButton2)
        button2.setOnClickListener {
            val intent = Intent(this@profile_activity, login_activity::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<View>(R.id.turnstiles_button)
        button3.setOnClickListener {
            val intent = Intent(this@profile_activity, security_activity::class.java)
            startActivity(intent)
        }
    }
}
