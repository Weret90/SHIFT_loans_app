package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.*
import com.umbrella.ermolaevshiftapp.domain.usecase.InsertPersonUseCase
import com.umbrella.ermolaevshiftapp.domain.usecase.CreateLoanUseCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetLoanConditionsUseCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetPersonUseCase
import com.umbrella.ermolaevshiftapp.presentation.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.NumberFormatException
import java.lang.RuntimeException

class CreateLoanViewModel(
    private val getLoanConditionsUseCase: GetLoanConditionsUseCase,
    private val createLoanUseCase: CreateLoanUseCase,
    private val getPersonUseCase: GetPersonUseCase,
    private val insertPersonUseCase: InsertPersonUseCase,
) : ViewModel() {

    private val _ifPersonExistInDatabaseLiveData = MutableLiveData<Person>()
    val ifPersonExistInDatabaseLiveData: LiveData<Person> = _ifPersonExistInDatabaseLiveData

    private val _createLoanLiveData = MutableLiveData<State<Loan>>()
    val createLoanLiveData: LiveData<State<Loan>> get() = _createLoanLiveData

    private val _getLoanConditionsLiveData = MutableLiveData<State<LoanConditions>>()
    val getLoanConditionsLiveData: LiveData<State<LoanConditions>> get() = _getLoanConditionsLiveData

    companion object {
        private const val ERROR_INPUT_DATA = "Incorrect input data"
        private const val ERROR_EMPTY_FIELDS = "All fields must be filled"
    }

    fun tryToFindPersonInDatabaseByLogin(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val person = getPersonUseCase(login)
            withContext(Dispatchers.Main) {
                _ifPersonExistInDatabaseLiveData.value = person
            }
        }
    }

    fun getLoanConditions(token: String) {
        _getLoanConditionsLiveData.value = State.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loanConditions = getLoanConditionsUseCase(token)
                withContext(Dispatchers.Main) {
                    _getLoanConditionsLiveData.value = State.Success(loanConditions)
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _getLoanConditionsLiveData.value =
                        State.Error(httpException.response()?.errorBody()?.string())
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _getLoanConditionsLiveData.value = State.Error(exception.message)
                }
            }
        }
    }

    fun createLoan(
        token: String,
        login: String,
        firstName: String,
        lastName: String,
        amount: String,
        percent: String,
        period: String,
        phoneNumber: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loanRequest =
                    parseInputData(firstName, lastName, amount, percent, period, phoneNumber)
                withContext(Dispatchers.Main) {
                    _createLoanLiveData.value = State.Loading
                }
                val loan = createLoanUseCase(token, loanRequest)
                insertPersonUseCase(Person(login, firstName, lastName))
                withContext(Dispatchers.Main) {
                    _createLoanLiveData.value = State.Success(loan)
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _createLoanLiveData.value =
                        State.Error(httpException.response()?.errorBody()?.string())
                }
            } catch (numberFormatException: NumberFormatException) {
                withContext(Dispatchers.Main) {
                    _createLoanLiveData.value = State.Error(ERROR_INPUT_DATA)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _createLoanLiveData.value = State.Error(exception.message)
                }
            }
        }
    }

    private fun parseInputData(
        firstName: String,
        lastName: String,
        amount: String,
        percent: String,
        period: String,
        phoneNumber: String,
    ): LoanRequest {
        if (
            firstName.isNotBlank()
            && lastName.isNotBlank()
            && amount.isNotBlank()
            && percent.isNotBlank()
            && period.isNotBlank()
            && phoneNumber.isNotBlank()
        ) {
            firstName.forEach { if (it.isDigit()) throw RuntimeException(ERROR_INPUT_DATA) }
            lastName.forEach { if (it.isDigit()) throw RuntimeException(ERROR_INPUT_DATA) }
            return LoanRequest(
                amount.trim().toInt(),
                firstName.trim(),
                lastName.trim(),
                percent.trim().toDouble(),
                period.trim().toInt(),
                phoneNumber.trim()
            )
        } else {
            throw RuntimeException(ERROR_EMPTY_FIELDS)
        }
    }

    fun clearCreateLoanLiveData() {
        _createLoanLiveData.value = null
    }

    fun clearGetLoanConditionsLiveData() {
        _getLoanConditionsLiveData.value = null
    }
}