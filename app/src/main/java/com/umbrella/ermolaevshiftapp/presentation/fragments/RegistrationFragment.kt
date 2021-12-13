package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentRegistrationBinding
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.presentation.*
import com.umbrella.ermolaevshiftapp.presentation.state.InputDataError
import com.umbrella.ermolaevshiftapp.presentation.state.State
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
        with(binding) {
            when (state) {
                is State.Loading -> {
                    loadingBar.show()
                    registrationLayout.hide()
                }
                is State.Success<User> -> {
                    context.showToast(
                        String.format(getString(R.string.registration_success), state.data.role)
                    )
                    parentFragmentManager.popBackStack()
                }
                is State.Error -> {
                    loadingBar.hide()
                    registrationLayout.show()
                    renderError(state)
                }
            }
        }
        viewModel.clearRegistrationLiveData()
    }

    private fun renderError(error: State.Error) {
        when (error) {
            is State.Error.ErrorResponse -> {
                when (error.errorCode) {
                    400 -> {
                        context.showToast(getString(R.string.error_user_already_exist))
                    }
                    else -> {
                        context.showToast(getString(R.string.error_failure_registration))
                        context.showToast(error.errorBody)
                    }
                }
            }
            is State.Error.InputError -> {
                when (error.inputDataError) {
                    InputDataError.EMPTY_INPUT_DATA -> {
                        context.showToast(getString(R.string.error_empty_input_data))
                    }
                    InputDataError.INCORRECT_INPUT_DATA -> {
                        //проверять валидность заполнения полей при регистрации в моем приложении не требуется
                    }
                }
            }
            is State.Error.UnknownError -> {
                context.showToast(getString(R.string.unknown_error_check_internet_connection))
                context.showToast(error.exception.message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}