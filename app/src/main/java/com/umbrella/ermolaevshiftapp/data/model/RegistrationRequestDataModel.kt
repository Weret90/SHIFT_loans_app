package com.umbrella.ermolaevshiftapp.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequestDataModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String
)