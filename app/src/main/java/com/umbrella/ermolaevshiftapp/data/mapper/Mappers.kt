package com.umbrella.ermolaevshiftapp.data.mapper

import com.umbrella.ermolaevshiftapp.data.model.*
import com.umbrella.ermolaevshiftapp.domain.entity.*

fun Auth.toDataModel() = AuthDataModel(
    name,
    password
)

fun UserDataModel.toDomainModel() = User(
    name,
    role
)

fun LoanConditionsDataModel.toDomainModel() = LoanConditions(
    maxAmount,
    percent,
    period
)

fun LoanRequest.toDataModel() = LoanRequestDataModel(
    amount,
    firstName,
    lastName,
    percent,
    period,
    phoneNumber
)

fun LoanDataModel.toDomainModel() = Loan(
    amount,
    date,
    firstName,
    id,
    lastName,
    percent,
    period,
    phoneNumber,
    state
)

fun List<LoanDataModel>.toDomainModel() = this.map {
    it.toDomainModel()
}