package com.umbrella.ermolaevshiftapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonDataModel(
    @PrimaryKey
    val login: String,
    val firstName: String,
    val lastName: String
)