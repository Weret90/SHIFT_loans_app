package com.umbrella.ermolaevshiftapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.R
import com.umbrella.ermolaevshiftapp.data.network.RetrofitInstance
import com.umbrella.ermolaevshiftapp.data.repository.LoansRepositoryImpl
import com.umbrella.ermolaevshiftapp.databinding.FragmentAuthorizationBinding
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAuthTokenUseCase
import com.umbrella.ermolaevshiftapp.presentation.State
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.AuthorizationViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.AuthorizationViewModelFactory

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val factory: AuthorizationViewModelFactory by lazy {
        AuthorizationViewModelFactory(GetAuthTokenUseCase(LoansRepositoryImpl(RetrofitInstance.api)))
    }
    private val viewModel: AuthorizationViewModel by lazy {
        ViewModelProvider(this, factory)[AuthorizationViewModel::class.java]
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
        when (state) {
            is State.Loading -> {
                binding.loadingBar.visibility = View.VISIBLE
                binding.authorizationLayout.visibility = View.GONE
            }
            is State.Success -> {
                binding.loadingBar.visibility = View.GONE
                binding.authorizationLayout.visibility = View.VISIBLE
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container,
                        MainFragment.newInstance(state.data.first.name, state.data.second))
                    .addToBackStack(null)
                    .commit()
            }
            is State.Error -> {
                binding.loadingBar.visibility = View.GONE
                binding.authorizationLayout.visibility = View.VISIBLE
                showToast(state.errorMessage)
            }
        }
        viewModel.clearAuthorizationLiveData()
    }

    private fun showToast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}