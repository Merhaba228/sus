package com.example.loginapp.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class RecordBooks(
    @SerializedName("Cod")
    val cod: String,
    @SerializedName("Number")
    val number: String,
    @SerializedName("LastYear")
    val lastYear: Int,
    @SerializedName("Course")
    val course: String,
    @SerializedName("Faculty")
    val faculty: String,
    @SerializedName("EducationForm")
    val educationForm: String,
    @SerializedName("EducationLevel")
    val educationLevel: String,
    @SerializedName("EducationBasis")
    val educationBasis: String,
    @SerializedName("Specialty")
    val specialty: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("Disable")
    val disable: Boolean
)
