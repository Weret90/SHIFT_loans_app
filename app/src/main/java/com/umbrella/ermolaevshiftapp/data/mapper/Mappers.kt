package com.umbrella.ermolaevshiftapp.data.mapper

import com.umbrella.ermolaevshiftapp.data.model.AuthDataModel
import com.umbrella.ermolaevshiftapp.data.model.UserDataModel
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User

fun Auth.toDataModel() = AuthDataModel(
    name,
    password
)

fun UserDataModel.toDomainModel() = User(
    name,
    role
)