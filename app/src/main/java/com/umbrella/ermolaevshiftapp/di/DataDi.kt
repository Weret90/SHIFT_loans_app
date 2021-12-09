package com.umbrella.ermolaevshiftapp.di

import com.umbrella.ermolaevshiftapp.data.network.RetrofitService
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object DataDi {

    private const val BASE_URL = "https://focusstart.appspot.com/"

    val repositoryModule = module {
        single<LoansRepository> {
            LoansRepositoryImpl(api = get())
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
}