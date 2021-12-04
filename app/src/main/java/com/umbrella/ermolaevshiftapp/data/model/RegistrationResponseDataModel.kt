package com.umbrella.ermolaevshiftapp.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponseDataModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: String
)