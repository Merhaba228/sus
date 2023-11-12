package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class Sections(
    @SerializedName("ControlDots")
    val controlDots: List<ControlDots>,
    @SerializedName("SectionType")
    val sectionType: OldRatingPlanSectionType,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Order")
    val order: Int,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("CreatorId")
    val creatorId: String,
    @SerializedName("CreateDate")
    val createDate: String
)
