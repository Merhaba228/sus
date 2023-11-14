package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Discipline(
    @SerializedName("Relevance")
    val relevance: Boolean,
    @SerializedName("IsTeacher")
    val isTeacher: Boolean,
    @SerializedName("UnreadedCount")
    val unreadedCount: Int,
    @SerializedName("UnreadedMessageCount")
    val unreadedMessageCount: Int,
    @SerializedName("Groups")
    val groups: List<String>,
    @SerializedName("DocFiles")
    val docFiles: List <DocFiles>,
    @SerializedName("WorkingProgramm")
    val workingProgramm: DocFiles,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("PlanNumber")
    val planNumber: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Faculty")
    val faculty: String,
    @SerializedName("EducationForm")
    val educationForm: String,
    @SerializedName("EducationLevel")
    val educationLevel: String,
    @SerializedName("Speciality")
    val speciality: String,
    @SerializedName("SpecialityCod")
    val specialityCod: String,
    @SerializedName("Profile")
    val profile: String,
    @SerializedName("PeriodString")
    val periodString: String,
    @SerializedName("PeriodInt")
    val periodInt: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Language")
    val language: String
)
