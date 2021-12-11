package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentLoansBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.presentation.*
import com.umbrella.ermolaevshiftapp.presentation.adapters.LoansAdapter
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.LoansViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoansFragment : Fragment() {

    private var _binding: FragmentLoansBinding? = null
    private val binding get() = _binding!!

    private val token by lazy {
        requireArguments().getString(KEY_TOKEN, "")
    }

    private val viewModel by viewModel<LoansViewModel>()

    private val loansAdapter: LoansAdapter by lazy {
        LoansAdapter()
    }

    companion object {

        private const val KEY_TOKEN = "token"
        private const val NOTIFICATION_ABOUT_LOANS_WORK = "notificationAboutLoans"

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
        with(binding) {
            when (state) {
                is State.Loading -> {
                    loadingBar.show()
                    emptyLoansListTv.hide()
                }
                is State.Success -> {
                    loadingBar.hide()
                    if (state.data.isEmpty()) {
                        emptyLoansListTv.show()
                    } else {
                        loansAdapter.setData(state.data)
                        startNotificationBackgroundTask()
                    }
                }
                is State.Error -> {
                    loadingBar.hide()
                    root.showSnackBar(
                        state.errorMessage, getString(R.string.snackbar_action_text)
                    ) { viewModel.getAllLoans(token) }
                }
            }
        }
        viewModel.clearLoansListLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startNotificationBackgroundTask() {
        WorkManager.getInstance(requireActivity()).enqueueUniqueWork(
            NOTIFICATION_ABOUT_LOANS_WORK,
            ExistingWorkPolicy.REPLACE,
            NotificationWorker.makeRequest()
        )
    }
}