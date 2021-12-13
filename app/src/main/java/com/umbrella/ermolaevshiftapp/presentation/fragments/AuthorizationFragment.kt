package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.databinding.FragmentAuthorizationBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.presentation.*
import com.umbrella.ermolaevshiftapp.presentation.state.InputDataError
import com.umbrella.ermolaevshiftapp.presentation.state.State
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.AuthorizationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AuthorizationViewModel>()

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val DEFAULT_LANGUAGE = "ru"
        private const val ENGLISH_LANGUAGE = "en"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authorizationLiveData.observe(viewLifecycleOwner) { authorizationState ->
            authorizationState?.let {
                renderData(authorizationState)
            }
        }

        binding.localizationButton.setOnClickListener {
            changeAppLanguage()
        }

        binding.buttonEnter.setOnClickListener {
            val name = binding.nameInputField.text.toString()
            val password = binding.passwordInputField.text.toString()
            viewModel.toEnter(name, password)
        }

        binding.buttonToRegistrationFragment.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, RegistrationFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun renderData(state: State<Pair<Auth, String>>) {
        with(binding) {
            when (state) {
                is State.Loading -> {
                    loadingBar.show()
                    authorizationLayout.hide()
                    localizationButton.hide()
                }
                is State.Success -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_container,
                            MainFragment.newInstance(state.data.first.name, state.data.second))
                        .addToBackStack(null)
                        .commit()
                }
                is State.Error -> {
                    loadingBar.hide()
                    authorizationLayout.show()
                    localizationButton.show()
                    renderError(state)
                }
            }
        }
        viewModel.clearAuthorizationLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderError(error: State.Error) {
        when (error) {
            is State.Error.ErrorResponse -> {
                when (error.errorCode) {
                    404 -> {
                        context.showToast(getString(R.string.error_wrong_password_or_user_not_exist))
                    }
                    else -> {
                        context.showToast(getString(R.string.error_failure_authorization))
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
                        //проверять валидность заполнения полей при авторизации в моем приложении не требуется
                    }
                }
            }
            is State.Error.UnknownError -> {
                context.showToast(getString(R.string.unknown_error_check_internet_connection))
                context.showToast(error.exception.message)
            }
        }
    }

    private fun changeAppLanguage() {
        val language = requireActivity()
            .getPreferences(MODE_PRIVATE)
            .getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        if (language == DEFAULT_LANGUAGE) {
            requireActivity()
                .getPreferences(MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, ENGLISH_LANGUAGE)
                .apply()
        } else {
            requireActivity()
                .getPreferences(MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, DEFAULT_LANGUAGE)
                .apply()
        }
        requireActivity().recreate()
    }
}