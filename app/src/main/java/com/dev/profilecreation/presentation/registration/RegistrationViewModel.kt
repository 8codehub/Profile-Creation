package com.dev.profilecreation.presentation.registration

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dev.profilecreation.data.remote.dto.User
import com.dev.profilecreation.data.remote.param.RegistrationParams
import com.dev.profilecreation.domain.model.ValidationResult
import com.dev.profilecreation.domain.use_case.*
import com.dev.profilecreation.presentation.core.BaseViewModel
import com.dev.profilecreation.presentation.core.ButtonState
import com.dev.profilecreation.presentation.core.InputFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RegistrationViewModel @Inject constructor(
    private val getBitmap: GetBitmap,
    private val validateImagePath: ValidateImagePath,
    private val validateEmail: ValidateEmail,
    private val registerUser: RegisterUser,
    private val validatePassword: ValidatePassword
) : BaseViewModel<RegistrationFormState, RegistrationIntent, RegistrationSingleEvent>() {

    override suspend fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.ValidPassword,
            is RegistrationIntent.InvalidPassword,
            is RegistrationIntent.ValidEmail,
            is RegistrationIntent.InvalidEmail      -> sendIntent(
                RegistrationIntent.ValidateSubmitButtonState
            )

            is RegistrationIntent.PasswordType      -> handlePasswordType(
                intent
            )
            is RegistrationIntent.ImageCaptured     -> handleImageCaptured(
                intent
            )
            is RegistrationIntent.EmailType         -> handleEmailType(
                intent.input
            )
            is RegistrationIntent.SubmitButtonClick -> handleSubmitButtonClick()
            else                                    -> Unit
        }
    }

    override fun reduceIntent(intent: RegistrationIntent, state: RegistrationFormState) = when (intent) {
        is RegistrationIntent.ValidateSubmitButtonState -> state.copy(
            submitButtonState = state.submitButtonState.copy(
                enabled = isSubmitButtonEnabled(
                    state
                )
            )
        )
        is RegistrationIntent.BitmapReady               -> state.copy(avatar = intent.input, avatarErrorMessage = null)

        is RegistrationIntent.InvalidImagePath          -> state.copy(avatar = null, avatarErrorMessage = intent.input)

        is RegistrationIntent.PasswordType              -> state.copy(passwordInput = state.passwordInput.copy(value = intent.input))
        is RegistrationIntent.InvalidPassword           -> state.copy(passwordInput = state.passwordInput.copy(error = intent.message))
        is RegistrationIntent.ValidPassword             -> state.copy(
            passwordInput = state.passwordInput.copy(
                error = null
            )
        )
        is RegistrationIntent.EmailType                 -> state.copy(emailInput = state.emailInput.copy(value = intent.input))
        is RegistrationIntent.ValidEmail                -> state.copy(
            emailInput = state.emailInput.copy(error = null), submitButtonState = state.submitButtonState.copy(
                enabled = isSubmitButtonEnabled(
                    state
                )
            )
        )
        is RegistrationIntent.InvalidEmail              -> state.copy(
            emailInput = state.emailInput.copy(error = intent.message), submitButtonState = state.submitButtonState.copy(
                enabled = isSubmitButtonEnabled(
                    state
                )
            )
        )
        is RegistrationIntent.FirstNameType             -> state.copy(
            firstNameInput = state.firstNameInput.copy(
                value = intent.input, error = null
            )
        )
        is RegistrationIntent.WebSiteType               -> state.copy(
            webSiteInput = state.webSiteInput.copy(
                value = intent.input, error = null
            )
        )
        is RegistrationIntent.SubmitButtonClick         -> state.copy(
            isBlockingLoading = true
        )
        is RegistrationIntent.UserCreated               -> state.copy(
            isBlockingLoading = false
        )
        else                                            -> state
    }

    private fun handlePasswordType(event: RegistrationIntent.PasswordType) {
        when (val result = validatePassword(event.input)) {
            is ValidationResult.Success -> {
                sendIntent(RegistrationIntent.ValidPassword)
            }
            is ValidationResult.Error   -> {
                sendIntent(RegistrationIntent.InvalidPassword(result.message))
            }
        }
    }

    private fun handleImageCaptured(event: RegistrationIntent.ImageCaptured) {
        when (validateImagePath(event.input)) {
            is ValidationResult.Success -> {
                val picture = getBitmap(event.input)
                if (picture == null) {
                    sendIntent(RegistrationIntent.BitmapResultFailed("Failed to create Bitmap"))
                } else {
                    sendIntent(RegistrationIntent.BitmapReady(picture))
                }
            }
            is ValidationResult.Error   -> {
                sendIntent(RegistrationIntent.InvalidImagePath("Failed to create Bitmap"))
            }
        }
    }

    private suspend fun handleSubmitButtonClick() {
        val stateValue = viewState.value
        val email = stateValue.emailInput.value ?: ""
        val password = stateValue.passwordInput.value ?: ""
        val params = RegistrationParams(
            avatar = stateValue.avatar, firstName = stateValue.firstNameInput.value, password = password, email = email,
            webSite = stateValue.webSiteInput.value
        )
        registerUser(params).collect {
            sendIntent(RegistrationIntent.UserCreated)
            triggerSingleEvent(RegistrationSingleEvent.NavigateUserProfile(it.id))
        }
    }


    private fun handleEmailType(input: String) {
        when (val validateResult = validateEmail(input)) {
            is ValidationResult.Success -> {
                sendIntent(RegistrationIntent.ValidEmail)
            }
            is ValidationResult.Error   -> {
                sendIntent(RegistrationIntent.InvalidEmail(validateResult.message))
            }
        }
    }

    private fun isSubmitButtonEnabled(state: RegistrationFormState) = state.passwordInput.error == null && state.emailInput.error == null

    override fun initState() = RegistrationFormState(
        avatar = null, avatarErrorMessage = null, firstNameInput = InputFieldState(),
        passwordInput = InputFieldState(error = "Password field is required"),
        emailInput = InputFieldState(error = "Email field is required"), submitButtonState = ButtonState(enabled = false),
        webSiteInput = InputFieldState(), isBlockingLoading = false
    )
}
