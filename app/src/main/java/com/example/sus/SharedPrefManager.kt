import android.content.Context
import android.content.SharedPreferences
import com.example.loginapp.activity.logic.auth.retrofit.api.*
import com.example.loginapp.activity.logic.auth.retrofit.dto.*
import com.example.sus.activity.logic.auth.retrofit.dto.*
import com.google.gson.Gson
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.withContext


object SharedPrefManager {
    private const val PREF_NAME = "MyPrefs"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val EXPIRATION_TIME = "expiration_time"
    private const val STUDENT_DATA = "student_data"
    private const val USER_DATA = "user_data"
    private const val SECURITY_EVENTS = "security_events"
    private const val CURRENT_DISCIPLINE = "current_discipline"
    private const val STUDENT_TIME_TABLE = "student_time_table"
    private const val STUDENT_SEMESTER = "student_semester"
    private const val STUDENT_RATING_PLAN = "student_rating_plan"
    private const val EVENTS = "events"
    private const val EVENT_DATA = "event_data"
    private const val NEWS_LIST = "news_list"

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
            refreshDataUsingRefreshToken()
        }
    }

    fun refreshDataUsingRefreshToken() {

        val BASE_URL_USER = "https://papi.mrsu.ru"

        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {

                checkTokenExpiration()

                val currentAccessToken = getAccessToken()

                val refreshedUserData = userApi.getUser("Bearer ${currentAccessToken}")
                saveUserData(refreshedUserData)

                val refreshedStudentData = userApi.getStudent("Bearer ${currentAccessToken}")
                saveStudentData(refreshedStudentData)

                val securityEvents = userApi.getSecurityEvents("Bearer ${currentAccessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                saveSecurityEvents(securityEvents)

                val studentTimeTable = userApi.getStudentTimeTable("Bearer ${currentAccessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                val studentPlanNumber = studentTimeTable[1].planNumber
                saveStudentTimeTable(studentTimeTable)

                val studentSemester = userApi.getStudentSemester("Bearer ${currentAccessToken}")
                saveStudentSemester(studentSemester)

                val events = userApi.getEventsByDate("Bearer ${currentAccessToken}", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
                saveEvents(events)

                val news = userApi.getNews("Bearer ${currentAccessToken}")
                saveNewsList(news)

            } catch (e: Exception) {

            }
        }

    }

    fun saveToken(userToken: Token) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, userToken.accessToken)
            putString(REFRESH_TOKEN, userToken.refreshToken)
            putLong(EXPIRATION_TIME, userToken.expiresIn * 1000 + System.currentTimeMillis() + 150)
            apply()
        }
    }

    fun saveStudentRatingPlan(studentRatingPlan: StudentRatingPlan) {
        val jsonStudentRatingPlan = Gson().toJson(studentRatingPlan)
        sharedPreferences.edit().apply {
            putString(STUDENT_RATING_PLAN, jsonStudentRatingPlan)
            apply()
        }
    }

    fun getStudentRatingPlan(): StudentRatingPlan {
        val jsonStudentSemester = sharedPreferences.getString(STUDENT_RATING_PLAN, null)
        return Gson().fromJson(jsonStudentSemester, StudentRatingPlan::class.java)
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

    fun getEvent(): Event? {
        val jsonEventData = sharedPreferences.getString(EVENT_DATA, null)
        return Gson().fromJson(jsonEventData, Event::class.java)
    }

    fun saveEvent(event: Event) {
        val jsonEventData = Gson().toJson(event)
        sharedPreferences.edit().apply {
            putString(EVENT_DATA, jsonEventData)
            apply()
        }
    }

    fun saveNewsList(newsList: List<News>) {
        val jsonNewsList = Gson().toJson(newsList)
        sharedPreferences.edit().apply {
            putString(NEWS_LIST, jsonNewsList)
            apply()
        }
    }

    fun getNewsList(): List<News>? {
        val jsonNewsList = sharedPreferences.getString(NEWS_LIST, null)
        val type: Type = object : TypeToken<List<News>>() {}.type
        return Gson().fromJson(jsonNewsList, type)
    }

    fun refreshNewsListUsingRefreshToken(callback: (List<News>) -> Unit) {
        val BASE_URL = "https://papi.mrsu.ru"
        val newsApi = createRetrofitApi(BASE_URL)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                Log.d("Check_news_1", "123")
                val currentAccessToken = getAccessToken()
                Log.d("Check_news_2", "123")
                val refreshedNewsList = newsApi.getNews("Bearer ${currentAccessToken}")
                Log.d("Check_news_3", "123")
                saveNewsList(refreshedNewsList)

                callback(refreshedNewsList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    fun saveStudentTimeTable(studentTimeTable: List<StudentTimeTable>) {
        val jsonStudentTimeTable = Gson().toJson(studentTimeTable)
        sharedPreferences.edit().apply {
            putString(STUDENT_TIME_TABLE, jsonStudentTimeTable)
            apply()
        }
    }

    fun getEvents(): List<EventInfo>? {
        val jsonEvents = sharedPreferences.getString(EVENTS, null)
        val type: Type = object : TypeToken<List<EventInfo>>() {}.type
        return Gson().fromJson(jsonEvents, type)
    }

    fun saveEvents(eventInfoList: List<EventInfo>) {
        val jsonEvents = Gson().toJson(eventInfoList)
        sharedPreferences.edit().apply {
            putString(EVENTS, jsonEvents)
            apply()
        }
    }


    fun getStudentSemester(): StudentSemester? {
        val jsonStudentSemester = sharedPreferences.getString(STUDENT_SEMESTER, null)
        return Gson().fromJson(jsonStudentSemester, StudentSemester::class.java)
    }

    fun saveStudentSemester(studentSemester: StudentSemester) {
        val jsonStudentSemester = Gson().toJson(studentSemester)
        sharedPreferences.edit().apply {
            putString(STUDENT_SEMESTER, jsonStudentSemester)
            apply()
        }
    }

    fun getStudentTimeTable(): List<StudentTimeTable>? {
        val jsonStudentTimeTable = sharedPreferences.getString(STUDENT_TIME_TABLE, null)
        val type: Type = object : TypeToken<List<StudentTimeTable>>() {}.type
        return Gson().fromJson(jsonStudentTimeTable, type)
    }

    fun saveDiscipline(discipline: Discipline) {
        val jsonDiscipline = Gson().toJson(discipline)
        sharedPreferences.edit().apply {
            putString(CURRENT_DISCIPLINE, jsonDiscipline)
            apply()
        }
    }

    fun getDiscipline(): Discipline? {
        val jsonDiscipline = sharedPreferences.getString(CURRENT_DISCIPLINE, null)
        return Gson().fromJson(jsonDiscipline, Discipline::class.java)
    }

    fun refreshStudentSemesterByDateUsingRefreshToken(year: String, period: Int) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()

                val refreshedStudentSemester = userApi.getStudentSemester("Bearer ${getAccessToken()}", year, period)
                saveStudentSemester(refreshedStudentSemester)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshEventUsingRefreshToken(eventid: String, callback: (Event) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()

                val refreshedEvent = userApi.getEventById("Bearer ${getAccessToken()}", eventid)
                saveEvent(refreshedEvent)
                callback(refreshedEvent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshAllEventUsingRefreshToken(callback: (List <EventInfo>) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                Log.d("Check_events_1", getAccessToken().toString())
                val refreshedEvents = userApi.getEvents("Bearer ${getAccessToken()}")
                Log.d("Check_events_2", "123")
                saveEvents(refreshedEvents)
                Log.d("Check_events_3", "123")
                callback(refreshedEvents)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshCalendarDateUsingRefreshToken(date: String, callback: (List<SecurityEvent>) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()

                val refreshedSecurityEvents = userApi.getSecurityEvents("Bearer ${getAccessToken()}", date)
                saveSecurityEvents(refreshedSecurityEvents)

                callback(refreshedSecurityEvents)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshEventsByDateUsingRefreshToken(date: String, callback: (List<EventInfo>) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                Log.d("Check_events_1", "123")
                val refreshedEvents = userApi.getEventsByDate("Bearer ${getAccessToken()}", date)
                Log.d("Check_events_2", "123")
                saveEvents(refreshedEvents)
                Log.d("Check_events_3", "123")

                callback(refreshedEvents)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun refreshCurrentDisciplineUsingRefreshToken(disciplineId: String, callback: (Discipline) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()

                val refreshedCurrentDiscipline = userApi.getDisciplineById("Bearer ${getAccessToken()}", disciplineId.toInt())
                saveDiscipline(refreshedCurrentDiscipline)

                callback(refreshedCurrentDiscipline)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshTimeTableDateUsingRefreshToken(date: String, callback: (List<StudentTimeTable>) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                val refreshedStudentTimeTable = userApi.getStudentTimeTable("Bearer ${getAccessToken()}", date)
                saveStudentTimeTable(refreshedStudentTimeTable)

                callback(refreshedStudentTimeTable)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshStudentRatingPlanUsingRefreshToken(disciplineId: String, callback: (StudentRatingPlan) -> Unit) {
        val BASE_URL_USER = "https://papi.mrsu.ru"
        val userApi = createRetrofitApi(BASE_URL_USER)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                checkTokenExpiration()
                val refreshedStudentRatingPlan = userApi.getStudentRatingPlan("Bearer ${getAccessToken()}", disciplineId.toInt())
                saveStudentRatingPlan(refreshedStudentRatingPlan)

                callback(refreshedStudentRatingPlan)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun getExpTime(): Long {
        return sharedPreferences.getLong(EXPIRATION_TIME, 0)
    }

    fun clearData() {
                sharedPreferences.edit().clear().apply()
        }


    private fun createRetrofitApi(baseUrl: String): MrsuApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MrsuApi::class.java)
    }

    private fun isAccessTokenExpired(): Boolean {
        val currentTime = System.currentTimeMillis()

        return currentTime >= getExpTime()
    }

    private fun checkTokenExpiration() {
        if (isAccessTokenExpired())
        {
            val BASE_URL_TOKEN = "https://p.mrsu.ru"
            val tokenApi = createRetrofitApi(BASE_URL_TOKEN)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userToken = tokenApi.getNewToken(refreshToken = getRefreshToken().toString())
                    saveToken(userToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}
