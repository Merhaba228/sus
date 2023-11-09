package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class RecordBooks_StudentSemester(
    @SerializedName("Cod")
    val cod: String,
    @SerializedName("Number")
    val number: String,
    @SerializedName("Faculty")
    val faculty: String,
    @SerializedName("Disciplines")
    val discipline: List<Discipline>
)