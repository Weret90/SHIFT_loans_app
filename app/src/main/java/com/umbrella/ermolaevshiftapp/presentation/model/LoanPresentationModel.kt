package com.umbrella.ermolaevshiftapp.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoanPresentationModel(
    val amount: Int,
    val date: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: String,
) : Parcelable