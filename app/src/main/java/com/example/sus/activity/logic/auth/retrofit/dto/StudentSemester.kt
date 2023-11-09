package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class StudentSemester(
    @SerializedName("RecordBooks")
    val recordBooks: List<RecordBooks_StudentSemester>,
    @SerializedName("UnreadedDisCount")
    val unreadedDisCount: Int,
    @SerializedName("UnreadedDisMesCount")
    val unreadedDisMesCount: Int,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Period")
    val period: Int,
)
