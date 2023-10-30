package com.dev.profilecreation.domain.model

sealed interface ValidationResult {
    object Success : ValidationResult
    data class Error(val message: String) : ValidationResult
}

