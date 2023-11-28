package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Grant(
    @SerializedName("ModeratedItemInfo")
    val moderatedItemInfo: ModeratedItemInfo,

    @SerializedName("Type")
    val type: Boolean,

    @SerializedName("TypeString")
    val typeString: String,

    @SerializedName("IsInternational")
    val isInternational: Boolean,

    @SerializedName("InternationalString")
    val internationalString: String,

    @SerializedName("IsUMNIK")
    val isUMNIK: Boolean,

    @SerializedName("GrantingOrganization")
    val grantingOrganization: String,

    @SerializedName("Amount")
    val amount: Double,

    @SerializedName("Currency")
    val currency: Int,

    @SerializedName("CurrencyString")
    val currencyString: String
)

data class ModeratedItemInfo(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: Int,

    @SerializedName("Creator")
    val creator: UserCrop,

    @SerializedName("Moderator")
    val moderator: UserCrop,

    @SerializedName("StatusCode")
    val statusCode: ModeratedItemStatus,

    @SerializedName("ModerateStatusExplanation")
    val moderateStatusExplanation: String,

    @SerializedName("CreateDate")
    val createDate: String,

    @SerializedName("LastEditDate")
    val lastEditDate: String,

    @SerializedName("StatusLastEditDate")
    val statusLastEditDate: String,

    @SerializedName("StatusMessage")
    val statusMessage: String,

    @SerializedName("Percentages")
    val percentages: List<ModeratedItemPercentage>
)

enum class ModeratedItemStatus(val value: Int) {
    @SerializedName("Disable")
    DISABLED(-1),

    @SerializedName("Unconfirmed")
    UNCONFIRMED(0),

    @SerializedName("Confirmed")
    CONFIRMED(1),

    @SerializedName("Rejected")
    REJECTED(2),

    @SerializedName("Returned")
    RETURNED(3)
}

data class ModeratedItemPercentage(
    @SerializedName("Member")
    val member: UserCrop,

    @SerializedName("Faculty")
    val faculty: String,

    @SerializedName("CafId")
    val cafId: Int,

    @SerializedName("Caf")
    val caf: String,

    @SerializedName("Percentage")
    val percentage: Double
)