package com.umbrella.ermolaevshiftapp.di

import androidx.room.Room
import com.umbrella.ermolaevshiftapp.data.database.PersonsDatabase
import com.umbrella.ermolaevshiftapp.data.network.RetrofitService
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.data.repository.PersonsRepositoryImpl
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository
import com.umbrella.ermolaevshiftapp.domain.repository.PersonsRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object DataDi {

    private const val BASE_URL = "https://focusstart.appspot.com/"
    private const val DATABASE_NAME = "users.db"

    val repositoryModule = module {
        single<LoansRepository> {
            LoansRepositoryImpl(api = get())
        }

        single<PersonsRepository> {
            PersonsRepositoryImpl(dao = get())
        }
    }

    val retrofitModule = module {
        single<RetrofitService> {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }

    val roomModule = module {
        single {
            Room.databaseBuilder(get(), PersonsDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
                .personsDao()
        }
    }
}