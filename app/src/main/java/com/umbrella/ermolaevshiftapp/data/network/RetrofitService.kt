package com.umbrella.ermolaevshiftapp.data.network

import com.umbrella.ermolaevshiftapp.data.model.AuthDataModel
import com.umbrella.ermolaevshiftapp.data.model.UserDataModel
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("registration")
    suspend fun toRegister(@Body auth: AuthDataModel): UserDataModel

    @POST("login")
    suspend fun getAuthToken(@Body auth: AuthDataModel): String
}