package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class StudentRatingPlan(
    @SerializedName("MarkZeroSession")
    val markZeroSession: MarkZeroSession,

    @SerializedName("Sections")
    val sections: List<Sections>

)
