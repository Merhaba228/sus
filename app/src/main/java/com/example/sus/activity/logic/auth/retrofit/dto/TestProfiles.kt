package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class TestProfiles(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("TestTitle")
    val testTitle: String,
    @SerializedName("Creator")
    val creator: UserCrop
)