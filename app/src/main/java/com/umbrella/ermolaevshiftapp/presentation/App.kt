package com.umbrella.ermolaevshiftapp.presentation

import android.app.Application
import com.umbrella.ermolaevshiftapp.di.DataDi
import com.umbrella.ermolaevshiftapp.di.DomainDi
import com.umbrella.ermolaevshiftapp.di.PresentationDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                DataDi.repositoryModule,
                DataDi.retrofitModule,
                DataDi.roomModule,
                DomainDi.useCasesModule,
                PresentationDi.viewModelsModule
            )
        }
    }
}