package com.umbrella.ermolaevshiftapp.domain.entity

data class LoanConditions(
    val maxAmount: Int,
    val percent: Double,
    val period: Int
)