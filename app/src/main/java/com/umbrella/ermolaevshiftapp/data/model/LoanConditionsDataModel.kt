package com.umbrella.ermolaevshiftapp.data.model

import com.google.gson.annotations.SerializedName

data class LoanConditionsDataModel (
    @SerializedName("maxAmount")
    val maxAmount: Int,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("period")
    val period: Int
)