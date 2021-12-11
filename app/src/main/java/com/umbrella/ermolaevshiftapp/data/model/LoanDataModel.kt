package com.umbrella.ermolaevshiftapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class LoanDataModel(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("date")
    val date: Date,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("period")
    val period: Int,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("state")
    val state: String
)