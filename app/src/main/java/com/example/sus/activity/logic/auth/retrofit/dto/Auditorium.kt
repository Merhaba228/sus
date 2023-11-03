package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Auditorium(
    @SerializedName("Id") val id: Int,
    @SerializedName("Number") val number: String,
    @SerializedName("Title") val title: String,
    @SerializedName("CampusId") val campusId: Int,
    @SerializedName("CampusTitle") val campusTitle: String
)
