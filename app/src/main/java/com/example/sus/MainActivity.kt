package com.example.sus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sus.R
import com.example.loginapp.activity.logic.auth.retrofit.api.MrsuApi
import com.example.loginapp.activity.logic.auth.retrofit.dto.AuthRequest
import com.example.sus.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private val MRSU_URL: String = "https://p.mrsu.ru"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.editUsername)
        password = findViewById(R.id.editPassword)
        loginButton = findViewById(R.id.enterButton)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(MRSU_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mrsuApi = retrofit.create(MrsuApi::class.java)

        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
             try {
                 val userToken = mrsuApi.getToken(
                     username = username.text.toString(),
                     password = password.text.toString()
                 )

                 runOnUiThread {
                     performActionsAfterAuthentication()

                 }
             } catch (e: Exception) {runOnUiThread {
                 Toast.makeText(
                     this@MainActivity,
                     "Неверный логин или пароль",
                     Toast.LENGTH_SHORT
                 ).show()
             }}

            }
        }
    }

    private fun performActionsAfterAuthentication() {
        val intent = Intent(this@MainActivity, MainActivity2::class.java)
        startActivity(intent)
    }
}