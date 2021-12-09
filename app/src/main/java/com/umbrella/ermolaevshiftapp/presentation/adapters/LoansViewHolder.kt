package com.umbrella.ermolaevshiftapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.ItemLoanBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import java.text.SimpleDateFormat
import java.util.*


class LoansViewHolder(private val binding: ItemLoanBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(loan: Loan) {
        binding.loanDate.text = String.format(
            binding.root.resources.getString(R.string.loan_date), convertTime(loan.date)
        )
        binding.loanStatus.text = String.format(
            binding.root.resources.getString(R.string.loan_status), loan.state
        )
        binding.loanAmount.text = String.format(
            binding.root.resources.getString(R.string.loan_amount), loan.amount
        )
    }

    private fun convertTime(time: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return formatter.format(parser.parse(time))
    }
}