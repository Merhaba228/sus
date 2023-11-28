package com.example.sus.activity.logic.auth.retrofit.dto
import com.google.gson.annotations.SerializedName

data class DigitalEducationalResource(
    @SerializedName("ModeratedItemInfo")
    val moderatedItemInfo: ModeratedItemInfo,

    @SerializedName("Type")
    val type: Boolean,

    @SerializedName("TypeString")
    val typeString: String,

    @SerializedName("Authors")
    val authors: String,

    @SerializedName("CertificateNumber")
    val certificateNumber: String,

    @SerializedName("RegistrationDate")
    val registrationDate: String,

    @SerializedName("Size")
    val size: Double
)
