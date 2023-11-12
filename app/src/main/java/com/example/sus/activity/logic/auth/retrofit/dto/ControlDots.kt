package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class ControlDots(
    @SerializedName("Mark")
    val mark: RatingMark,
    @SerializedName("Report")
    val report: StudentRatingPlanControlDotReport,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Order")
    val order: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("MaxBall")
    val maxBall: Double,
    @SerializedName("IsReport")
    val isReport: Boolean,
    @SerializedName("IsCredit")
    val isCredit: Boolean,
    @SerializedName("CreatorId")
    val creatorId: String,
    @SerializedName("CreateDate")
    val createDate: String,
    @SerializedName("TestProfiles")
    val testProfiles: List<TestProfiles>
)
