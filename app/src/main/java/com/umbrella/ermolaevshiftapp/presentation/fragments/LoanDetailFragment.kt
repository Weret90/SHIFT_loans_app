package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentLoanDetailBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.presentation.LoanStatus
import com.umbrella.ermolaevshiftapp.presentation.convertToString
import com.umbrella.ermolaevshiftapp.presentation.mapper.toPresentationModel
import com.umbrella.ermolaevshiftapp.presentation.model.LoanPresentationModel

class LoanDetailFragment : Fragment() {

    private var _binding: FragmentLoanDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        private const val KEY_LOAN = "loan"

        fun newInstance(loan: Loan): LoanDetailFragment {
            return LoanDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LOAN, loan.toPresentationModel())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().getParcelable<LoanPresentationModel>(KEY_LOAN)?.let { loan ->

            when (loan.state) {
                LoanStatus.APPROVED.name -> {
                    binding.loanStatus.setTextColor(Color.GREEN)
                }
                LoanStatus.REJECTED.name -> {
                    binding.loanStatus.setTextColor(Color.RED)
                }
                LoanStatus.REGISTERED.name -> {
                    binding.loanStatus.setTextColor(Color.BLUE)
                }
            }

            with(binding) {
                loanId.text = String.format(getString(R.string.loan_id), loan.id.toString())
                loanDate.text =
                    String.format(getString(R.string.loan_date), loan.date.convertToString())
                loanFirstName.text =
                    String.format(getString(R.string.loan_first_name), loan.firstName)
                loanLastName.text = String.format(getString(R.string.loan_last_name), loan.lastName)
                loanPhoneNumber.text =
                    String.format(getString(R.string.loan_phone_number), loan.phoneNumber)
                loanPercent.text =
                    String.format(getString(R.string.loan_percent), loan.percent.toString())
                loanPeriod.text =
                    String.format(getString(R.string.loan_period), loan.period.toString())
                loanAmount.text =
                    String.format(getString(R.string.loan_amount), loan.amount.toString())
                loanStatus.text = String.format(getString(R.string.loan_status), loan.state)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}