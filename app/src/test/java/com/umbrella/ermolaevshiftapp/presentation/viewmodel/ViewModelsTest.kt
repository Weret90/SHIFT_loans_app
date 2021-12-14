package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.umbrella.ermolaevshiftapp.di.DataDi
import com.umbrella.ermolaevshiftapp.di.DomainDi
import com.umbrella.ermolaevshiftapp.di.PresentationDi
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAuthTokenUseCase
import com.umbrella.ermolaevshiftapp.presentation.state.InputDataError
import com.umbrella.ermolaevshiftapp.presentation.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.kotlin.mock

class ViewModelsTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val context: Context = mock()
        startKoin {
            androidContext(context)
            modules(
                DataDi.repositoryModule,
                DataDi.retrofitModule,
                DataDi.roomModule,
                DomainDi.useCasesModule,
                PresentationDi.viewModelsModule
            )
        }
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `WHEN to register with empty fields EXPECT input data error`() {
        val viewModel: RegistrationViewModel by inject()
        viewModel.toRegister("", "")
        val result = viewModel.registrationLiveData.getOrAwaitMultipleValues()
        assertTrue(result.size == 1)
        assertTrue(result.contains(State.Error.InputError(InputDataError.EMPTY_INPUT_DATA)))
    }

    @Test
    fun `WHEN to try register existing user EXPECT error response with code 400`() {
        val viewModel: RegistrationViewModel by inject()
        viewModel.toRegister("weret", "123")
        val result = viewModel.registrationLiveData.getOrAwaitMultipleValues(2)
        assertTrue(result.size == 2) //учитывая state с loading
        assertTrue(result.contains(State.Error.ErrorResponse(400,
            "400 BAD_REQUEST \"User already exist\"")))
    }

    @Test
    fun `WHEN try to authorize with wrong password EXPECT error response with code 404`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: AuthorizationViewModel by inject()
            viewModel.toEnter("weret", "12345")
            val result = viewModel.authorizationLiveData.getOrAwaitMultipleValues(2)
            assertTrue(result.size == 2)
            assertTrue(result.contains(State.Error.ErrorResponse(404,
                "404 NOT_FOUND \"User not found\"")))
        }
    }

    @Test
    fun `WHEN authorize with correct login and password EXPECT success authorization`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: AuthorizationViewModel by inject()
            viewModel.toEnter("weret", "1")
            val result = viewModel.authorizationLiveData.getOrAwaitMultipleValues(2)
            assertTrue(result.size == 2)
            assertTrue(result[0] is State.Loading)
            assertTrue(result[1] is State.Success)
        }
    }

    @Test
    fun `WHEN get loans list with correct token EXPECT list with 12 loans`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: LoansViewModel by inject()
            val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            viewModel.getAllLoans(token)
            val result = viewModel.loansListLiveData.getOrAwaitMultipleValues(2)
            assertTrue(result.size == 2)
            assertTrue(result[0] is State.Loading)
            assertTrue((result[1] as State.Success).data.size == 12)
        }
    }

    @Test
    fun `WHEN create loan with empty field EXPECT error empty input data`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: CreateLoanViewModel by inject()
            val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            viewModel.createLoan(token, "Petro", "Petr", "Petrov", "", "12.5", "30", "911")
            val result = viewModel.createLoanLiveData.getOrAwaitMultipleValues(1)
            assertTrue(result.contains(State.Error.InputError(InputDataError.EMPTY_INPUT_DATA)))
            assertTrue(result.size == 1)
        }
    }

    @Test
    fun `WHEN create loan with incorrect firstName with numbers EXPECT error incorrect input data`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: CreateLoanViewModel by inject()
            val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            viewModel.createLoan(token, "Petro", "Petr90", "Petrov", "5000", "12.5", "30", "911")
            val result = viewModel.createLoanLiveData.getOrAwaitMultipleValues(1)
            assertTrue(result.contains(State.Error.InputError(InputDataError.INCORRECT_INPUT_DATA)))
            assertTrue(result.size == 1)
        }
    }

    @Test
    fun `WHEN create loan with incorrect loan conditions EXPECT error response with code 400`() {
        runBlocking(Dispatchers.Main) {
            val viewModel: CreateLoanViewModel by inject()
            val getAuthTokenUseCase: GetAuthTokenUseCase by inject()
            val token = getAuthTokenUseCase(Auth("weret", "1"))
            viewModel.createLoan(token, "Petro", "Petr", "Petrov", "5000", "12.5", "30", "911")
            val result = viewModel.createLoanLiveData.getOrAwaitMultipleValues(2)
            assertTrue(result.size == 2)
            assertTrue(result[0] is State.Loading)
            assertTrue((result[1] as State.Error.ErrorResponse).errorCode == 400)
        }
    }
}