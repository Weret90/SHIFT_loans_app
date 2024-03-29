package com.umbrella.ermolaevshiftapp.data.repository

import com.umbrella.ermolaevshiftapp.data.mapper.toDataModel
import com.umbrella.ermolaevshiftapp.data.mapper.toDomainModel
import com.umbrella.ermolaevshiftapp.data.network.RetrofitService
import com.umbrella.ermolaevshiftapp.domain.entity.*
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class LoansRepositoryImpl(private val api: RetrofitService) : LoansRepository {

    override suspend fun toRegister(auth: Auth): User {
        val response = api.toRegister(auth.toDataModel())
        return response.toDomainModel()
    }

    override suspend fun getAuthToken(auth: Auth): String {
        return api.getAuthToken(auth.toDataModel())
    }

    override suspend fun getLoanConditions(token: String): LoanConditions {
        return api.getLoanConditions(token).toDomainModel()
    }

    override suspend fun createLoan(token: String, loanRequest: LoanRequest): Loan {
        return api.createLoan(token, loanRequest.toDataModel()).toDomainModel()
    }

    override suspend fun getAllLoans(token: String): List<Loan> {
        return api.getAllLoans(token).toDomainModel()
    }
}