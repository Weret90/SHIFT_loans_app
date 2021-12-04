package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.data.network.RetrofitInstance
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.databinding.FragmentRegistrationBinding
import com.umbrella.ermolaevshiftapp.domain.usecase.ToRegisterUseCase
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.RegistrationViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.RegistrationViewModelFactory

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val factory: RegistrationViewModelFactory by lazy {
        RegistrationViewModelFactory(ToRegisterUseCase(LoansRepositoryImpl(RetrofitInstance.api)))
    }
    private val viewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this, factory)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.success.observe(viewLifecycleOwner) {
            showToast("Регистрация прошла успешно: $it")
        }

        viewModel.error.observe(viewLifecycleOwner) {
            showToast(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) { shouldShowLoadingBar ->
            if (shouldShowLoadingBar) {
                binding.loadingBar.visibility = View.VISIBLE
                binding.registrationLayout.visibility = View.GONE
            } else {
                binding.loadingBar.visibility = View.GONE
                binding.registrationLayout.visibility = View.VISIBLE
            }
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.nameInputField.text.toString()
            val password = binding.passwordInputField.text.toString()
            viewModel.toRegister(name, password)
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