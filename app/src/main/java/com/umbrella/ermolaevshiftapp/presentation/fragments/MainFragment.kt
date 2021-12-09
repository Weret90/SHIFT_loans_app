package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {

        private const val KEY_TOKEN = "token"
        private const val KEY_NAME = "name"

        fun newInstance(name: String, token: String): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TOKEN, token)
                    putString(KEY_NAME, name)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireArguments().getString(KEY_NAME, "")
        binding.welcomeTextView.text = String.format(getString(R.string.welcome_text_view), name)

        val token = requireArguments().getString(KEY_TOKEN, "")

        binding.buttonCreateLoan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, CreateLoanFragment.newInstance(token))
                .addToBackStack(null)
                .commit()
        }

        binding.buttonMyLoans.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LoansFragment.newInstance(token))
                .addToBackStack(null)
                .commit()
        }
    }
}