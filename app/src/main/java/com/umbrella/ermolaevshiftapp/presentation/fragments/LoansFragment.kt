package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentLoansBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.presentation.State
import com.umbrella.ermolaevshiftapp.presentation.adapters.LoansAdapter
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.LoansViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoansFragment : Fragment() {

    private var _binding: FragmentLoansBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String

    private val viewModel by viewModel<LoansViewModel>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = requireArguments().getString(KEY_TOKEN, "")
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

        viewModel.getAllLoans(token)

        loansAdapter.onLoanItemClickListener = {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoanDetailFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }

        viewModel.loansListLiveData.observe(viewLifecycleOwner) { getLoansListState ->
            getLoansListState?.let {
                renderData(getLoansListState)
            }
        }
    }

    private fun renderData(state: State<List<Loan>>) {
        when (state) {
            is State.Loading -> {
                binding.loansRv.visibility = View.GONE
                binding.loadingBar.visibility = View.VISIBLE
                binding.emptyLoansListTv.visibility = View.GONE
            }
            is State.Success -> {
                binding.loansRv.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                if (state.data.isEmpty()) {
                    binding.emptyLoansListTv.visibility = View.VISIBLE
                } else {
                    loansAdapter.setData(state.data)
                }
            }
            is State.Error -> {
                binding.loansRv.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                showSnackBar(state.errorMessage)
            }
        }
        viewModel.clearLoansListLiveData()
    }

    private fun showSnackBar(text: String?) {
        text?.let {
            Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_action_text)) {
                    viewModel.getAllLoans(token)
                }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}