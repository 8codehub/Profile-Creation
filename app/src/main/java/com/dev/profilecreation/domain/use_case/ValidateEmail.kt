package com.dev.profilecreation.domain.use_case

import android.util.Patterns
import com.dev.profilecreation.domain.model.ValidationResult
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String?) = when {
        email == null || email.isBlank()                -> ValidationResult.Error("The email can't be blank")
        Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Success
        else                                            -> ValidationResult.Error("That's not a valid email")
    }
}
