package com.umbrella.ermolaevshiftapp.data.repository

import com.umbrella.ermolaevshiftapp.data.mapper.toDataModel
import com.umbrella.ermolaevshiftapp.data.mapper.toDomainModel
import com.umbrella.ermolaevshiftapp.data.network.RetrofitService
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationRequest
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationResponse
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class LoansRepositoryImpl(private val api: RetrofitService) : LoansRepository {

    override suspend fun toRegister(registrationRequest: RegistrationRequest): RegistrationResponse {
        val response = api.toRegister(registrationRequest.toDataModel())
        return response.toDomainModel()
    }
}