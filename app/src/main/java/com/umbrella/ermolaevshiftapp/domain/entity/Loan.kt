package com.umbrella.ermolaevshiftapp.domain.entity

import java.util.*

data class Loan(
    val amount: Int,
    val date: Date,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: String
)