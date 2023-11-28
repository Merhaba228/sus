package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("Id")
    val userId: String,

    @SerializedName("IsTeacher")
    val isTeacher: Boolean,

    @SerializedName("IsStudent")
    val isStudent: Boolean,

    @SerializedName("FIO")
    val fullName: String,

    @SerializedName("EnglishFIO")
    val englishFullName: String
)
