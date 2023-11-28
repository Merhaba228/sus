package com.example.loginapp.activity.logic.auth.retrofit.api

import com.example.loginapp.activity.logic.auth.retrofit.dto.*
import com.example.sus.activity.logic.auth.retrofit.dto.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Query

interface MrsuApi {
    @GET("v1/User")
    suspend fun getUser(@Header("Authorization") authorization: String): User

    @GET("v1/StudentInfo")
    suspend fun getStudent(@Header("Authorization") authorization: String): Student

    @GET("v1/Security")
    suspend fun getSecurityEvents(
        @Header("Authorization") authorization: String,
        @Query("date") date: String
    ): List<SecurityEvent>

    @GET("v1/StudentsGroup")
    suspend fun getStudentsGroup(
        @Header("Authorization") authorization: String,
        @Query("group") group: String,
        @Query("planNumber") planNumber: String,
        @Query("disciplineId") disciplineId: Int
    ): List<UserCrop>

    @GET("v1/Event")
    suspend fun getEventById(
        @Header("Authorization") authorization: String,
        @Query("eventid") eventId: String
    ): Event

    @GET("v1/Events")
    suspend fun getEventsByDate(
        @Header("Authorization") authorization: String,
        @Query("date") date: String
    ): List<EventInfo>

    @GET("v1/News")
    suspend fun getNews(
        @Header("Authorization") authorization: String,
    ): List<News>

    @GET("v1/Events")
    suspend fun getEvents(
        @Header("Authorization") authorization: String,
    ): List<EventInfo>

    @GET("v1/Events")
    suspend fun getEventsByMode(
        @Header("Authorization") authorization: String,
        @Query("mode") mode: String
    ): List<EventInfo>

    @GET("v1/Grant")
    suspend fun getGrants(
        @Header("Authorization") authorization: String,
        @Query("type") type: Boolean
    ): List<Grant>

    @GET("v1/NIOKR")
    suspend fun getNIOKR(
        @Header("Authorization") authorization: String
    ): List<NIOKR>

    @GET("v1/DigitalEducationalResource")
    suspend fun getDigitalEducationalResources(
        @Header("Authorization") authorization: String,
        @Query("type") type: Boolean = false
    ): List<DigitalEducationalResource>

    @GET("v1/Publication")
    suspend fun getPublications(
        @Header("Authorization") authorization: String,
        @Query("publicationType") publicationType: Int? = null
    ): List<Publication>

    @GET("v1/StudentTimeTable")
    suspend fun getStudentTimeTable(
        @Header("Authorization") authorization: String,
        @Query("date") date: String
    ): List<StudentTimeTable>

    @GET("v1/StudentSemester?selector=current")
    suspend fun getStudentSemester(
        @Header ("Authorization") authorization: String): StudentSemester

    @GET("v1/StudentSemester")
    suspend fun getStudentSemester(
        @Header("Authorization") authorization: String,
        @Query("year") year: String,
        @Query("period") period: Int): StudentSemester

    @GET("v1/StudentRatingPlan")
    suspend fun getStudentRatingPlan(
        @Header ("Authorization") authorization: String,
        @Query ("id") id: Int): StudentRatingPlan

    @GET("v1/Discipline")
    suspend fun getDisciplineById(
        @Header("Authorization") authorization: String,
        @Query("id") id: Int): Discipline

    @FormUrlEncoded
    @POST("OAuth/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String = "8",
        @Field("client_secret") clientSecret: String = "qweasd"
    ): Token

    @FormUrlEncoded
    @POST("OAuth/token")
    suspend fun getNewToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = "8",
        @Field("client_secret") clientSecret: String = "qweasd"
    ): Token
}
