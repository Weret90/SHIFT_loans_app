package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.data.network.RetrofitInstance
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.databinding.FragmentCreateLoanBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.entity.LoanConditions
import com.umbrella.ermolaevshiftapp.domain.usecase.CreateLoanUSeCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetLoanConditionsUseCase
import com.umbrella.ermolaevshiftapp.presentation.State
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModelFactory

class CreateLoanFragment : Fragment() {

    private var _binding: FragmentCreateLoanBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String

    private val factory: CreateLoanViewModelFactory by lazy {
        CreateLoanViewModelFactory(
            GetLoanConditionsUseCase(LoansRepositoryImpl(RetrofitInstance.api)),
            CreateLoanUSeCase(LoansRepositoryImpl(RetrofitInstance.api))
        )
    }
    private val viewModel: CreateLoanViewModel by lazy {
        ViewModelProvider(this, factory)[CreateLoanViewModel::class.java]
    }

    companion object {

        private const val KEY_TOKEN = "token"

        fun newInstance(token: String): CreateLoanFragment {
            return CreateLoanFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TOKEN, token)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = requireArguments().getString(KEY_TOKEN, "")
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

        binding.buttonCreateLoan.setOnClickListener {
            viewModel.createLoan(
                token,
                binding.nameEt.text.toString(),
                binding.lastNameEt.text.toString(),
                binding.amountEt.text.toString(),
                binding.percentEt.text.toString(),
                binding.periodEt.text.toString(),
                binding.phoneNumberEt.text.toString()
            )
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
        when (state) {
            is State.Loading -> {
                binding.createLoanInputFields.visibility = View.GONE
                binding.loadingBar.visibility = View.VISIBLE
            }
            is State.Success -> {
                binding.createLoanInputFields.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                showToast("Займ успешно оформлен!")
                parentFragmentManager.popBackStack()
            }
            is State.Error -> {
                binding.createLoanInputFields.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                showToast(state.errorMessage)
            }
        }
        viewModel.clearCreateLoanLiveData()
    }

    private fun renderDataLoanConditions(state: State<LoanConditions>) {
        when (state) {
            is State.Loading -> {
                binding.loadingBar.visibility = View.VISIBLE
                binding.createLoanInputFields.visibility = View.GONE
                binding.loanConditionsCardView.visibility = View.GONE
            }
            is State.Success -> {
                binding.loadingBar.visibility = View.GONE
                binding.createLoanInputFields.visibility = View.VISIBLE
                binding.loanConditionsCardView.visibility = View.VISIBLE
                binding.conditionPercent.text = String.format(
                    getString(R.string.condition_percent),
                    state.data.percent.toString()
                )
                binding.conditionPeriod.text = String.format(
                    getString(R.string.condition_period),
                    state.data.period.toString()
                )
                binding.conditionMaxAmount.text = String.format(
                    getString(R.string.condition_max_amount),
                    state.data.maxAmount.toString()
                )
            }
            is State.Error -> {
                binding.loadingBar.visibility = View.GONE
                showSnackBar(state.errorMessage)
            }
        }
        viewModel.clearGetLoanConditionsLiveData()
    }

    private fun showToast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun showSnackBar(text: String?) {
        text?.let {
            Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_action_text)) {
                    viewModel.getLoanConditions(token)
                }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}