package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

enum class OldRatingPlanSectionType {
    @SerializedName("Экзамен")
    EXAM,
    @SerializedName("Текущий")
    DEFAULT,
    @SerializedName("Курсовая")
    PROJECT
}
