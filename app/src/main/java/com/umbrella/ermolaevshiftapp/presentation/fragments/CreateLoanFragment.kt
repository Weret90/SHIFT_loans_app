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
import com.umbrella.ermolaevshiftapp.domain.usecase.CreateLoanUSeCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetLoanConditionsUseCase
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModelFactory

class CreateLoanFragment : Fragment() {

    private var _binding: FragmentCreateLoanBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateLoanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = requireArguments().getString(KEY_TOKEN, "")

        viewModel.getLoanConditions(token)

        binding.buttonCreateLoan.setOnClickListener {
            viewModel.createLoan(
                token,
                binding.nameEt.text.toString(),
                binding.lastNameEt.text.toString(),
                binding.amountEt.text.toString(),
                binding.percentEt.text.toString(),
                binding.percentEt.text.toString(),
                binding.phoneNumberEt.text.toString()
            )
        }

        viewModel.successLoanConditions.observe(viewLifecycleOwner) {
            binding.createLoanInputFields.visibility = View.VISIBLE
            binding.loanConditionsCardView.visibility = View.VISIBLE
            binding.conditionPercent.text = String.format(
                getString(R.string.condition_percent),
                it.percent.toString()
            )
            binding.conditionPeriod.text = String.format(
                getString(R.string.condition_period),
                it.period.toString()
            )
            binding.conditionMaxAmount.text = String.format(
                getString(R.string.condition_max_amount),
                it.maxAmount.toString()
            )
        }

        viewModel.errorLoanConditions.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE).setAction("Reload") {
                viewModel.getLoanConditions(token)
            }.show()
        }

        viewModel.loadingLoanConditions.observe(viewLifecycleOwner) { shouldShowLoadingBar ->
            if (shouldShowLoadingBar) {
                binding.loadingBar.visibility = View.VISIBLE
                binding.createLoanInputFields.visibility = View.GONE
                binding.loanConditionsCardView.visibility = View.GONE
            } else {
                binding.loadingBar.visibility = View.GONE
            }
        }

        viewModel.successLoan.observe(viewLifecycleOwner) {
            showToast("Займ успешно оформлен!")
            parentFragmentManager.popBackStack()
        }

        viewModel.loadingLoan.observe(viewLifecycleOwner) { shouldShowLoadingBar ->
            if (shouldShowLoadingBar) {
                binding.createLoanInputFields.visibility = View.GONE
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.createLoanInputFields.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
            }
        }

        viewModel.errorLoan.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}