package com.umbrella.ermolaevshiftapp.data.mapper

import com.umbrella.ermolaevshiftapp.data.model.RegistrationRequestDataModel
import com.umbrella.ermolaevshiftapp.data.model.RegistrationResponseDataModel
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationRequest
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationResponse

fun RegistrationRequest.toDataModel() = RegistrationRequestDataModel(
    name,
    password
)

fun RegistrationResponseDataModel.toDomainModel() = RegistrationResponse(
    name,
    role
)