package com.dev.profilecreation.domain.model


data class Registration(
    val firstName: String? = null,
    val email: String,
    val password: String,
    val image: Any?,
    val website: String?
)
