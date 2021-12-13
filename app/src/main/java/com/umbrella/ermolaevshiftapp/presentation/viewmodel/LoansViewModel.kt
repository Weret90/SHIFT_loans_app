package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAllLoansUseCase
import com.umbrella.ermolaevshiftapp.presentation.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoansViewModel(
    private val getAllLoansUseCase: GetAllLoansUseCase,
) : ViewModel() {

    private val _loansListLiveData = MutableLiveData<State<List<Loan>>>()
    val loansListLiveData: LiveData<State<List<Loan>>> get() = _loansListLiveData


    fun getAllLoans(token: String) {
        _loansListLiveData.value = State.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loans = getAllLoansUseCase(token)
                withContext(Dispatchers.Main) {
                    _loansListLiveData.value = State.Success(loans)
                }
            } catch (httpException: HttpException) {
                withContext(Dispatchers.Main) {
                    _loansListLiveData.value = State.Error.ErrorResponse(
                        httpException.code(), httpException.response()?.errorBody()?.string()
                    )
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    _loansListLiveData.value = State.Error.UnknownError(exception)
                }
            }
        }
    }

    fun clearLoansListLiveData() {
        _loansListLiveData.value = null
    }
}