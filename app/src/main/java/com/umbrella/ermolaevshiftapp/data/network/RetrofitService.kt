package com.umbrella.ermolaevshiftapp.data.network

import com.umbrella.ermolaevshiftapp.data.model.RegistrationRequestDataModel
import com.umbrella.ermolaevshiftapp.data.model.RegistrationResponseDataModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("registration")
    suspend fun toRegister(@Body request: RegistrationRequestDataModel): RegistrationResponseDataModel
}