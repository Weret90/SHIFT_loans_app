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
                    context.showToast("Займ №${state.data.id} успешно оформлен!")
                    parentFragmentManager.popBackStack()
                }
                is State.Error -> {
                    loadingBar.hide()
                    createLoanInputFields.show()
                    loanConditionsCardView.show()
                    context.showToast(state.errorMessage)
                }
            }
        }
        viewModel.clearCreateLoanLiveData()
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
                    root.showSnackBar(
                        state.errorMessage, getString(R.string.snackbar_action_text)
                    ) { viewModel.getLoanConditions(token) }
                }
            }
        }
        viewModel.clearGetLoanConditionsLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}