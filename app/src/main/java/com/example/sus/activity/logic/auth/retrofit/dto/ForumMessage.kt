package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class ForumMessage(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("User")
    val user: UserCrop,
    @SerializedName("IsTeacher")
    val isTeacher: Boolean,
    @SerializedName("CreateDate")
    val createDate: String,
    @SerializedName("Text")
    val text: String
)
