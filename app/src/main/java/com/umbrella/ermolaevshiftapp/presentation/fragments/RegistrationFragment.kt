package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.databinding.FragmentRegistrationBinding
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.presentation.State
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<RegistrationViewModel>()

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
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

        viewModel.registrationLiveData.observe(viewLifecycleOwner) { registrationState ->
            registrationState?.let {
                renderData(registrationState)
            }
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.nameInputField.text.toString()
            val password = binding.passwordInputField.text.toString()
            viewModel.toRegister(name, password)
        }
    }

    private fun renderData(state: State<User>) {
        when (state) {
            is State.Loading -> {
                binding.loadingBar.visibility = View.VISIBLE
                binding.registrationLayout.visibility = View.GONE
            }
            is State.Success<User> -> {
                binding.loadingBar.visibility = View.GONE
                binding.registrationLayout.visibility = View.VISIBLE
                showToast("Регистрация прошла успешно: ${state.data}")
                parentFragmentManager.popBackStack()
            }
            is State.Error -> {
                binding.loadingBar.visibility = View.GONE
                binding.registrationLayout.visibility = View.VISIBLE
                showToast(state.errorMessage)
            }
        }
        viewModel.clearRegistrationLiveData()
    }

    private fun showToast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}