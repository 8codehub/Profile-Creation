package com.dev.profilecreation.data.remote.param

import android.graphics.Bitmap

data class RegistrationParams(
    val avatar: Bitmap?,
    val firstName: String?,
    val email: String,
    val password: String,
    val webSite: String?
)
