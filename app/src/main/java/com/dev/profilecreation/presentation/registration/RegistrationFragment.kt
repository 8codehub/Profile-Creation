package com.dev.profilecreation.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dev.profilecreation.presentation.registration.RegistrationFragmentDirections.actionRegistrationFragmentToProfileFragment
import com.dev.profilecreation.databinding.FragmentRegistrationBinding
import com.dev.profilecreation.domain.util.safeNavigate
import com.dev.profilecreation.presentation.core.BaseBindingFragment
import com.dev.profilecreation.presentation.core.ButtonState
import com.dev.profilecreation.presentation.core.InputFieldState
import com.dev.profilecreation.presentation.registration.RegistrationIntent.PasswordType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : BaseBindingFragment<FragmentRegistrationBinding>() {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegistrationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditTextListeners()
        initViews()
        initLiveData()
    }

    private fun initViews() {
        binding.pickAvatar.setOnClickListener {
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToCameraFragment())
        }
        binding.submitButton.setOnClickListener {
            viewModel.sendIntent(RegistrationIntent.SubmitButtonClick)
        }
    }

    private fun initLiveData() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("image_path")?.observe(
            viewLifecycleOwner
        ) { path ->
            viewModel.sendIntent(RegistrationIntent.ImageCaptured(path))
        }

        lifecycleScope.launch {
            viewModel.viewState.collect {
                updateAvatar(it)
                updatePasswordState(it.passwordInput)
                updateEmailState(it.emailInput)
                updateSubmitButtonState(it.submitButtonState)
                updateLoading(it.isBlockingLoading)
            }
        }

        lifecycleScope.launch {
            viewModel.singleIntent.collect {
                when (it) {
                    is RegistrationSingleEvent.NavigateUserProfile -> {
                        findNavController().safeNavigate(actionRegistrationFragmentToProfileFragment(it.id))
                    }
                }
            }
        }
    }

    private fun updateSubmitButtonState(state: ButtonState) {
        binding.submitButton.isEnabled = state.enabled
    }

    private fun updateLoading(isblockingLoading: Boolean) {
        binding.progressBar.visibility = if (isblockingLoading) View.VISIBLE else View.GONE
    }

    private fun updateEmailState(state: InputFieldState) {
        binding.email.error = state.error
    }

    private fun updatePasswordState(state: InputFieldState) {
        binding.password.error = state.error
    }

    private fun initEditTextListeners() {
        binding.firstName.doAfterTextChanged {
            viewModel.sendIntent(RegistrationIntent.FirstNameType(it.toString()))
        }

        binding.email.doAfterTextChanged {
            viewModel.sendIntent(RegistrationIntent.EmailType(it.toString()))
        }

        binding.password.doAfterTextChanged {
            viewModel.sendIntent(PasswordType(it.toString()))
        }
        binding.webSite.doAfterTextChanged {
            viewModel.sendIntent(RegistrationIntent.WebSiteType(it.toString()))
        }
    }

    private fun updateAvatar(formState: RegistrationFormState) {
        if (formState.avatarErrorMessage != null) {
            binding.userAvatar.setImageDrawable(null)
            Toast.makeText(requireContext(), formState.avatarErrorMessage, Toast.LENGTH_SHORT).show()
        } else {
            binding.userAvatar.setImageBitmap(formState.avatar)
        }
    }
}

