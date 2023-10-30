package com.dev.profilecreation.domain.use_case

import com.dev.profilecreation.domain.model.ValidationResult
import javax.inject.Inject

class ValidateImagePath @Inject constructor() {
    operator fun invoke(path: String?) = if (path == null || path.isBlank())
        ValidationResult.Error("The image path is not correct")
    else
        ValidationResult.Success
}
