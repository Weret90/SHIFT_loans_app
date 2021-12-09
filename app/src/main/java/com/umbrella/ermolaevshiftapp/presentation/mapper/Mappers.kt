package com.umbrella.ermolaevshiftapp.presentation.mapper

import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.presentation.model.LoanPresentationModel

fun Loan.toPresentationModel() = LoanPresentationModel(
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