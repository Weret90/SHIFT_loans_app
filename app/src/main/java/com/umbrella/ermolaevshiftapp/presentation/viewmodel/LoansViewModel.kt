package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAllLoansUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoansViewModel(
    private val getAllLoansUseCase: GetAllLoansUseCase,
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _emptyLoansList = MutableLiveData<Boolean>()
    val emptyLoansList: LiveData<Boolean> get() = _emptyLoansList

    private val _success = MutableLiveData<List<Loan>>()
    val success: LiveData<List<Loan>> get() = _success


    fun getAllLoans(token: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loans = getAllLoansUseCase(token)
                withContext(Dispatchers.Main) {
                    _emptyLoansList.value = loans.isEmpty()
                    _success.value = loans
                    _loading.value = false
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _emptyLoansList.value = false
                    _error.value = httpException.response()?.errorBody()?.string()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                    _emptyLoansList.value = false
                    _error.value = exception.message
                }
            }
        }
    }
}