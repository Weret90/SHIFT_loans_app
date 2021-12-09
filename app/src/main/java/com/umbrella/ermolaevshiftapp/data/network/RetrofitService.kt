package com.umbrella.ermolaevshiftapp.data.network

import com.umbrella.ermolaevshiftapp.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {

    @POST("registration")
    suspend fun toRegister(@Body auth: AuthDataModel): UserDataModel

    @POST("login")
    suspend fun getAuthToken(@Body auth: AuthDataModel): String

    @GET("loans/conditions")
    suspend fun getLoanConditions(@Header("Authorization") token: String): LoanConditionsDataModel

    @POST("loans")
    suspend fun createLoan(
        @Header("Authorization") token: String,
        @Body loanRequest: LoanRequestDataModel,
    ): LoanDataModel

    @GET("loans/all")
    suspend fun getAllLoans(@Header("Authorization") token: String): List<LoanDataModel>
}