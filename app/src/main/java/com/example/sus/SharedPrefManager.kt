import android.content.Context
import android.content.SharedPreferences
import com.example.loginapp.activity.logic.auth.retrofit.api.MrsuApi
import com.example.loginapp.activity.logic.auth.retrofit.dto.User
import com.example.loginapp.activity.logic.auth.retrofit.dto.SecurityEvent
import com.example.loginapp.activity.logic.auth.retrofit.dto.Student
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object SharedPrefManager {
    private const val PREF_NAME = "MyPrefs"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val STUDENT_DATA = "student_data"
    private const val USER_DATA = "user_data"
    private const val SECURITY_EVENTS = "security_events"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var instance: SharedPrefManager

    fun getInstance(context: Context): SharedPrefManager {
        if (!this::instance.isInitialized) {
            instance = SharedPrefManager
            instance.init(context)
        }
        return instance
    }

    private fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val refreshToken = REFRESH_TOKEN
        if (refreshToken != null) {
            refreshDataUsingRefreshToken(refreshToken)
        }
    }

    fun refreshDataUsingRefreshToken(refreshToken: String) {
        val BASE_URL_TOKEN = "https://p.mrsu.ru"
        val BASE_URL_USER = "https://papi.mrsu.ru"

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val tokenApi = createRetrofitClient(BASE_URL_TOKEN).create(MrsuApi::class.java)
        val userApi = createRetrofitClient(BASE_URL_USER).create(MrsuApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userToken = tokenApi.getNewToken(refreshToken = refreshToken)
                saveTokens(userToken.accessToken, userToken.refreshToken)

                val refreshedUserData = userApi.getUser("Bearer ${userToken.accessToken}")
                saveUserData(refreshedUserData)

                val refreshedStudentData = userApi.getStudent("Bearer ${userToken.accessToken}")
                saveStudentData(refreshedStudentData)


            } catch (e: Exception) {

            }
        }

    }
    fun createRetrofitClient(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun saveUserData(userData: User) {
        val jsonUserData = Gson().toJson(userData)
        sharedPreferences.edit().apply {
            putString(USER_DATA, jsonUserData)
            apply()
        }
    }

    fun getUserData(): User? {
        val jsonUserData = sharedPreferences.getString(USER_DATA, null)
        return Gson().fromJson(jsonUserData, User::class.java)
    }

    fun saveStudentData(studentData: Student) {
        val jsonStudentData = Gson().toJson(studentData)
        sharedPreferences.edit().apply {
            putString(STUDENT_DATA, jsonStudentData)
            apply()
        }
    }

    fun getStudentData(): Student? {
        val jsonStudentData = sharedPreferences.getString(STUDENT_DATA, null)
        return Gson().fromJson(jsonStudentData, Student::class.java)
    }

    fun saveSecurityEvents(securityEvents: List<SecurityEvent>) {
        val jsonSecurityEvents = Gson().toJson(securityEvents)
        sharedPreferences.edit().apply {
            putString(SECURITY_EVENTS, jsonSecurityEvents)
            apply()
        }
    }

    fun getSecurityEvents(): List<SecurityEvent>? {
        val jsonSecurityEvents = sharedPreferences.getString(SECURITY_EVENTS, null)
        val type: Type = object : TypeToken<List<SecurityEvent>>() {}.type
        return Gson().fromJson(jsonSecurityEvents, type)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun clearTokens() {
        sharedPreferences.edit().apply {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
            remove(USER_DATA)
            remove(STUDENT_DATA)
            apply()
        }

    }

}
