package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class RatingMark(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Ball")
    val ball: Double,
    @SerializedName("CreatorId")
    val creatorId: String,
    @SerializedName("CreateDate")
    val createDate: String
)