package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentCreateLoanBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.entity.LoanConditions
import com.umbrella.ermolaevshiftapp.presentation.*
import com.umbrella.ermolaevshiftapp.presentation.state.InputDataError
import com.umbrella.ermolaevshiftapp.presentation.state.State
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateLoanFragment : Fragment() {

    private var _binding: FragmentCreateLoanBinding? = null
    private val binding get() = _binding!!

    private val token: String by lazy {
        requireArguments().getString(KEY_TOKEN, "")
    }

    private val login: String by lazy {
        requireArguments().getString(KEY_LOGIN, "")
    }

    private val viewModel by viewModel<CreateLoanViewModel>()

    companion object {

        private const val KEY_TOKEN = "token"
        private const val KEY_LOGIN = "login"

        fun newInstance(token: String, login: String): CreateLoanFragment {
            return CreateLoanFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TOKEN, token)
                    putString(KEY_LOGIN, login)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateLoanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoanConditions(token)

        viewModel.tryToFindPersonInDatabaseByLogin(login)

        binding.buttonCreateLoan.setOnClickListener {
            with(binding) {
                viewModel.createLoan(
                    token,
                    login,
                    nameEt.getStringText(),
                    lastNameEt.getStringText(),
                    amountEt.getStringText(),
                    percentEt.getStringText(),
                    periodEt.getStringText(),
                    phoneNumberEt.getStringText()
                )
            }
        }

        viewModel.ifPersonExistInDatabaseLiveData.observe(viewLifecycleOwner) { person ->
            person?.let {
                binding.nameEt.setText(person.firstName)
                binding.lastNameEt.setText(person.lastName)
            }
        }

        viewModel.getLoanConditionsLiveData.observe(viewLifecycleOwner) { getLoanConditionsState ->
            getLoanConditionsState?.let {
                renderDataLoanConditions(getLoanConditionsState)
            }
        }

        viewModel.createLoanLiveData.observe(viewLifecycleOwner) { createLoanState ->
            createLoanState?.let {
                renderDataCreateLoan(createLoanState)
            }
        }
    }

    private fun renderDataCreateLoan(state: State<Loan>) {
        with(binding) {
            when (state) {
                is State.Loading -> {
                    loadingBar.show()
                    createLoanInputFields.hide()
                    loanConditionsCardView.hide()
                }
                is State.Success -> {
                    context.showToast(
                        String.format(getString(R.string.loan_successfully_created), state.data.id)
                    )
                    parentFragmentManager.popBackStack()
                }
                is State.Error -> {
                    loadingBar.hide()
                    createLoanInputFields.show()
                    loanConditionsCardView.show()
                    renderCreateLoanError(state)
                }
            }
        }
        viewModel.clearCreateLoanLiveData()
    }

    private fun renderCreateLoanError(error: State.Error) {
        when (error) {
            is State.Error.ErrorResponse -> {
                when (error.errorCode) {
                    400 -> {
                        context.showToast(getString(R.string.error_inconsistency_with_conditions))
                        context.showToast(error.errorBody)
                    }
                    else -> {
                        context.showToast(getString(R.string.error_failure_create_loan))
                        context.showToast(error.errorBody)
                    }
                }
            }
            is State.Error.InputError -> {
                when (error.inputDataError) {
                    InputDataError.EMPTY_INPUT_DATA -> {
                        context.showToast(getString(R.string.error_empty_input_data))
                    }
                    InputDataError.INCORRECT_INPUT_DATA -> {
                        context.showToast(getString(R.string.error_incorrect_input_data))
                    }
                }
            }
            is State.Error.UnknownError -> {
                context.showToast(getString(R.string.unknown_error_check_internet_connection))
                context.showToast(error.exception.message)
            }
        }
    }

    private fun renderDataLoanConditions(state: State<LoanConditions>) {
        with(binding) {
            when (state) {
                is State.Loading -> {
                    loadingBar.show()
                    createLoanInputFields.hide()
                    loanConditionsCardView.hide()
                }
                is State.Success -> {
                    loadingBar.hide()
                    createLoanInputFields.show()
                    loanConditionsCardView.show()
                    conditionPercent.text = String.format(
                        getString(R.string.condition_percent), state.data.percent.toString()
                    )
                    conditionPeriod.text = String.format(
                        getString(R.string.condition_period), state.data.period.toString()
                    )
                    conditionMaxAmount.text = String.format(
                        getString(R.string.condition_max_amount), state.data.maxAmount.toString()
                    )
                }
                is State.Error -> {
                    loadingBar.hide()
                    renderLoanConditionsError(state)
                }
            }
        }
        viewModel.clearGetLoanConditionsLiveData()
    }

    private fun renderLoanConditionsError(error: State.Error) {
        when (error) {
            is State.Error.ErrorResponse -> {
                context.showToast(error.errorBody)
                binding.root.showSnackBar(
                    getString(R.string.error_failure_get_loan_conditions),
                    getString(R.string.snackbar_action_text)
                ) {
                    viewModel.getLoanConditions(token)
                }
            }
            is State.Error.InputError -> {
                //заполнять поля и проверять их валидность при получении условий займа с сервера не требуется
            }
            is State.Error.UnknownError -> {
                context.showToast(error.exception.message)
                binding.root.showSnackBar(
                    getString(R.string.unknown_error_check_internet_connection),
                    getString(R.string.snackbar_action_text)
                ) {
                    viewModel.getLoanConditions(token)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}