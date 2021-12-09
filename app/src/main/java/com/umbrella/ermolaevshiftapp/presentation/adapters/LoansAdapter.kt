package com.umbrella.ermolaevshiftapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.ermolaevshiftapp.databinding.ItemLoanBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Loan

class LoansAdapter : RecyclerView.Adapter<LoansViewHolder>() {

    private var loans = listOf<Loan>()

    var onLoanItemClickListener: ((Loan) -> Unit)? = null

    fun setData(loans: List<Loan>) {
        this.loans = loans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoansViewHolder {
        val binding = ItemLoanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoansViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoansViewHolder, position: Int) {
        holder.bind(loans[position])
        holder.itemView.setOnClickListener {
            onLoanItemClickListener?.invoke(loans[position])
        }
    }

    override fun getItemCount(): Int {
        return loans.size
    }
}