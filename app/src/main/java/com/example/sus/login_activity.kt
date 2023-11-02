package com.example.sus

import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.activity.logic.auth.retrofit.api.MrsuApi
import com.example.loginapp.activity.logic.auth.retrofit.dto.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class login_activity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private val BASE_URL_TOKEN = "https://p.mrsu.ru"
    private val BASE_URL_USER = "https://papi.mrsu.ru"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefManager = SharedPrefManager.getInstance(this)

        username = findViewById(R.id.editUsername)
        password = findViewById(R.id.editPassword)
        loginButton = findViewById(R.id.enterButton)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val tokenApi = createRetrofitClient(BASE_URL_TOKEN).create(MrsuApi::class.java)
        val userApi = createRetrofitClient(BASE_URL_USER).create(MrsuApi::class.java)

        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userToken = tokenApi.getToken(
                        username = username.text.toString(),
                        password = password.text.toString()
                    )
                    handleTokenResponse(userToken, sharedPrefManager, userApi)
                } catch (e: Exception) {
                    handleTokenFailure(e)
                }
            }
        }
    }

    private fun handleTokenFailure(throwable: Throwable) {
        Log.e("error_global", throwable.message.toString())
        Log.e("error_local", throwable.localizedMessage)
        runOnUiThread {
            showErrorToast("Ошибка при авторизации")
        }
    }

    private fun handleTokenResponse(userToken: Token, sharedPrefManager: SharedPrefManager, userApi: MrsuApi) {
        if (userToken.accessToken != null) {
            Log.d("old_token",userToken.accessToken.toString() )
            sharedPrefManager.saveTokens(userToken.accessToken, userToken.refreshToken)
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val user = userApi.getUser("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveUserData(user)

                    val student = userApi.getStudent("Bearer ${userToken.accessToken}")
                    sharedPrefManager.saveStudentData(student)

                    val securityEvents = userApi.getSecurityEvents("Bearer ${userToken.accessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                    sharedPrefManager.saveSecurityEvents(securityEvents)

                    runOnUiThread {
                        performActionsAfterAuthentication()
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        showErrorToast("${Date()}, Ошибка при получении пользовательских данных в handleTokenResponse ")
                        Log.e("error_global", e.message.toString())
                        Log.e("error_local", e.localizedMessage)
                    }
                }
            }
        } else {
            runOnUiThread {
                showErrorToast("Ошибка при авторизации")
            }
        }
    }

    private fun performActionsAfterAuthentication() {
        val intent = Intent(this@login_activity, general_activity::class.java)
        startActivity(intent)
    }

    private fun createRetrofitClient(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
