package com.umbrella.ermolaevshiftapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.umbrella.ermolaevshiftapp.data.model.PersonDataModel

@Database(entities = [PersonDataModel::class], version = 1, exportSchema = false)
abstract class PersonsDatabase : RoomDatabase() {

    abstract fun personsDao(): PersonsDao
}