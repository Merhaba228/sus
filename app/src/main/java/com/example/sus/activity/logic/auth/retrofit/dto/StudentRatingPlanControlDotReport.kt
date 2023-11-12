package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class StudentRatingPlanControlDotReport(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("CreateDate")
    val createDate: String,
    @SerializedName("DocFiles")
    val docFiles: List<DocFiles>
)
