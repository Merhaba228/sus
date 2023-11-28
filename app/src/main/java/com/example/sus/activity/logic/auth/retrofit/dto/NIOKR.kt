package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class NIOKR(
    @SerializedName("ModeratedItemInfo")
    val moderatedItemInfo: ModeratedItemInfo,

    @SerializedName("Number")
    val number: String,

    @SerializedName("Amount")
    val amount: Double
)
