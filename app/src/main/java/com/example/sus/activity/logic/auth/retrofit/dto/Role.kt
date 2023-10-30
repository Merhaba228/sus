package com.example.loginapp.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Description")
    val description: String
)
