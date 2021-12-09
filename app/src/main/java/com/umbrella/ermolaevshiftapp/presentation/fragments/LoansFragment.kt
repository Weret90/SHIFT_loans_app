package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.data.network.RetrofitInstance
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.databinding.FragmentLoansBinding
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAllLoansUseCase
import com.umbrella.ermolaevshiftapp.presentation.adapters.LoansAdapter
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.LoansViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.LoansViewModelFactory

class LoansFragment : Fragment() {

    private var _binding: FragmentLoansBinding? = null
    private val binding get() = _binding!!

    private val factory: LoansViewModelFactory by lazy {
        LoansViewModelFactory(
            GetAllLoansUseCase(LoansRepositoryImpl(RetrofitInstance.api))
        )
    }
    private val viewModel: LoansViewModel by lazy {
        ViewModelProvider(this, factory)[LoansViewModel::class.java]
    }
    private val loansAdapter: LoansAdapter by lazy {
        LoansAdapter()
    }

    companion object {

        private const val KEY_TOKEN = "token"

        fun newInstance(token: String): LoansFragment {
            return LoansFragment().apply {
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
        _binding = FragmentLoansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loansRv.adapter = loansAdapter

        val token = requireArguments().getString(KEY_TOKEN, "")
        viewModel.getAllLoans(token)

        loansAdapter.onLoanItemClickListener = {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoanDetailFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }

        viewModel.success.observe(viewLifecycleOwner) {
            loansAdapter.setData(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE).setAction("Reload") {
                viewModel.getAllLoans(token)
            }.show()
        }

        viewModel.loading.observe(viewLifecycleOwner) { shouldShowLoadingBar ->
            if (shouldShowLoadingBar) {
                binding.loansRv.visibility = View.GONE
                binding.loadingBar.visibility = View.VISIBLE
            } else {
                binding.loansRv.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
            }
        }

        viewModel.emptyLoansList.observe(viewLifecycleOwner) { isEmptyLoansList ->
            if (isEmptyLoansList) {
                binding.emptyLoansListTv.visibility = View.VISIBLE
            } else {
                binding.emptyLoansListTv.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}