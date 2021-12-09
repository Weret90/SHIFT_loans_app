package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.entity.LoanConditions
import com.umbrella.ermolaevshiftapp.domain.entity.LoanRequest
import com.umbrella.ermolaevshiftapp.domain.usecase.CreateLoanUSeCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetLoanConditionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.NumberFormatException
import java.lang.RuntimeException

class CreateLoanViewModel(
    private val getLoanConditionsUseCase: GetLoanConditionsUseCase,
    private val createLoanUSeCase: CreateLoanUSeCase,
) : ViewModel() {

    private val _errorLoanConditions = MutableLiveData<String>()
    val errorLoanConditions: LiveData<String> get() = _errorLoanConditions

    private val _errorLoan = MutableLiveData<String>()
    val errorLoan: LiveData<String> get() = _errorLoan

    private val _loadingLoanConditions = MutableLiveData<Boolean>()
    val loadingLoanConditions: LiveData<Boolean> get() = _loadingLoanConditions

    private val _loadingLoan = MutableLiveData<Boolean>()
    val loadingLoan: LiveData<Boolean> get() = _loadingLoan

    private val _successLoanConditions = MutableLiveData<LoanConditions>()
    val successLoanConditions: LiveData<LoanConditions> get() = _successLoanConditions

    private val _successLoan = MutableLiveData<Loan>()
    val successLoan: LiveData<Loan> get() = _successLoan

    fun getLoanConditions(token: String) {
        _loadingLoanConditions.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loanConditions = getLoanConditionsUseCase(token)
                withContext(Dispatchers.Main) {
                    _loadingLoanConditions.value = false
                    _successLoanConditions.value = loanConditions
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _loadingLoanConditions.value = false
                    _errorLoanConditions.value = httpException.response()?.errorBody()?.string()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _loadingLoanConditions.value = false
                    _errorLoanConditions.value = exception.message
                }
            }
        }
    }

    fun createLoan(
        token: String,
        firstName: String,
        lastName: String,
        amount: String,
        percent: String,
        period: String,
        phoneNumber: String,
    ) {
        _loadingLoan.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loanRequest =
                    parseInputData(firstName, lastName, amount, percent, period, phoneNumber)
                val loan = createLoanUSeCase(token, loanRequest)
                withContext(Dispatchers.Main) {
                    _loadingLoan.value = false
                    _successLoan.value = loan
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _loadingLoan.value = false
                    _errorLoan.value = httpException.response()?.errorBody()?.string()
                }
            } catch (numberFormatException: NumberFormatException) {
                withContext(Dispatchers.Main) {
                    _loadingLoan.value = false
                    _errorLoan.value =
                        "Введены некорректные данные. Дробным может быть только процент, числа не должны содержать пробелы"
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _loadingLoan.value = false
                    _errorLoan.value = exception.message
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
            return LoanRequest(
                amount.trim().toInt(),
                firstName.trim(),
                lastName.trim(),
                percent.trim().toDouble(),
                period.trim().toInt(),
                phoneNumber.trim()
            )
        } else {
            throw RuntimeException("Некоторые поля не заполнены, заполните все поля")
        }
    }
}