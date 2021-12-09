package com.umbrella.ermolaevshiftapp.presentation

import android.app.Application
import com.umbrella.ermolaevshiftapp.di.DataDi
import com.umbrella.ermolaevshiftapp.di.DomainDi
import com.umbrella.ermolaevshiftapp.di.PresentationDi
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                DataDi.repositoryModule,
                DataDi.retrofitModule,
                DomainDi.useCasesModule,
                PresentationDi.viewModelsModule
            )
        }
    }
}