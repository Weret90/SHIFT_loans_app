package com.umbrella.ermolaevshiftapp.data.model

import com.google.gson.annotations.SerializedName

data class UserDataModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: String
)