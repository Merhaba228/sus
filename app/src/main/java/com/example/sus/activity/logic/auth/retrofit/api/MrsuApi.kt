package com.example.loginapp.activity.logic.auth.retrofit.api

import com.example.loginapp.activity.logic.auth.retrofit.dto.SecurityEvent
import com.example.loginapp.activity.logic.auth.retrofit.dto.User
import com.example.loginapp.activity.logic.auth.retrofit.dto.Token
import com.example.loginapp.activity.logic.auth.retrofit.dto.Student
import com.example.sus.activity.logic.auth.retrofit.dto.StudentTimeTable
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

    @GET("v1/StudentTimeTable")
    suspend fun getStudentTimeTable(
        @Header("Authorization") authorization: String,
        @Query("date") date: String
    ): List<StudentTimeTable>

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
