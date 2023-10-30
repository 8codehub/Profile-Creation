package com.dev.profilecreation.domain.use_case

import com.dev.profilecreation.domain.model.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    operator fun invoke(password: String?): ValidationResult {
        if (password == null || password.length < 8) {
            return ValidationResult.Error("The password needs to consist of at least 8 characters")
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult.Error("The password needs to contain at least one letter and digit")
        }
        return ValidationResult.Success
    }
}
