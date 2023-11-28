package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("Id") val id: Int,
    @SerializedName("Date") val date: String,
    @SerializedName("Text") val text: String,
    @SerializedName("Header") val header: String,
    @SerializedName("Viewed") val viewed: Boolean
)
