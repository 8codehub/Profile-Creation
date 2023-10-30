package com.dev.profilecreation.presentation.registration

import android.graphics.Bitmap
import com.dev.profilecreation.presentation.core.*

data class RegistrationFormState(
    val avatar: Bitmap?,
    val avatarErrorMessage: String?,
    val firstNameInput: InputFieldState,
    val emailInput: InputFieldState,
    val passwordInput: InputFieldState,
    val webSiteInput: InputFieldState,
    val submitButtonState: ButtonState,
    val isBlockingLoading: Boolean
) : State

sealed interface RegistrationIntent : Intent {
    data class ImageCaptured(val input: String) : RegistrationIntent
    data class BitmapReady(val input: Bitmap) : RegistrationIntent
    data class InvalidImagePath(val input: String) : RegistrationIntent
    data class BitmapResultFailed(val input: String) : RegistrationIntent
    data class PasswordType(val input: String) : RegistrationIntent
    data class InvalidPassword(val message: String) : RegistrationIntent
    object ValidPassword : RegistrationIntent

    data class EmailType(val input: String) : RegistrationIntent
    data class InvalidEmail(val message: String) : RegistrationIntent
    object ValidEmail : RegistrationIntent

    data class FirstNameType(val input: String) : RegistrationIntent
    data class WebSiteType(val input: String) : RegistrationIntent

    object ValidateSubmitButtonState : RegistrationIntent
    object SubmitButtonClick : RegistrationIntent
    object UserCreated : RegistrationIntent

}

sealed interface RegistrationSingleEvent : SingleEvent {
    data class NavigateUserProfile(val id: String) : RegistrationSingleEvent
}
