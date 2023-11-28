package com.example.loginapp.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class StudentsGroup(
    @SerializedName("Id")
    val id: String,

    @SerializedName("UserName")
    val userName: String,

    @SerializedName("FIO")
    val fullName: String,

    @SerializedName("Photo")
    val photo: UserPhoto
)
