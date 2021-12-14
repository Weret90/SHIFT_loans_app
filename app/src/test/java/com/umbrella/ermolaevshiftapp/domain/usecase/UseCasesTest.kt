package com.umbrella.ermolaevshiftapp.domain.usecase

import android.content.Context
import com.umbrella.ermolaevshiftapp.data.model.LoanConditionsDataModel
import com.umbrella.ermolaevshiftapp.data.network.RetrofitService
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.di.DataDi
import com.umbrella.ermolaevshiftapp.di.DomainDi
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.LoanConditions
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException

class UseCasesTest : KoinTest {

    @Before
    fun before() {
        val context: Context = mock()
        startKoin {
            androidContext(context)
            modules(DataDi.retrofitModule, DataDi.repositoryModule, DomainDi.useCasesModule)
        }
    }

    @Test(expected = HttpException::class)
    fun `WHEN register existing user EXPECT http exception`() {
        val toRegisterUseCase: ToRegisterUseCase by inject()
        runBlocking {
            toRegisterUseCase(Auth("weret", "1234"))
        }
    }

    @Test(expected = HttpException::class)
    fun `WHEN authorize non-existing user EXPECT http exception`() {
        val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
        runBlocking {
            getAuthTokenUseCase(Auth("noneExistingUser", "5678"))
        }
    }

    @Test
    fun `WHEN authorize existing user with right password EXPECT token`() {
        val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
        runBlocking {
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            assertTrue(token.startsWith("Bearer"))
        }
    }

    @Test(expected = HttpException::class)
    fun `WHEN authorize existing user with wrong password EXPECT http exception`() {
        val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
        runBlocking {
            getAuthTokenUseCase(Auth("weret", "1234"))
        }
    }

    @Test
    fun `WHEN get loans list with correct token EXPECT list with 12 loans`() {
        val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
        val getAllLoanUseCase: GetAllLoansUseCase by inject()
        runBlocking {
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            val loansList = getAllLoanUseCase(token)
            assertEquals(12, loansList.size)
        }
    }

    @Test
    fun `WHEN get loan conditions with correct token EXPECT correct result`() {
        val api: RetrofitService = mock()
        runBlocking {
            whenever(api.getLoanConditions("someCorrectToken")).thenReturn(
                LoanConditionsDataModel(
                    5000,
                    12.5,
                    20)
            )
            val getLoanConditionsUseCase = GetLoanConditionsUseCase(LoansRepositoryImpl(api))
            val actual = getLoanConditionsUseCase("someCorrectToken")
            val expected = LoanConditions(5000, 12.5, 20)
            assertEquals(expected, actual)
        }
    }

    @After
    fun after() {
        stopKoin()
    }
}