package com.dev.profilecreation.domain.model

import android.graphics.Bitmap

data class ProfileUserInfo(
    val userId: String,
    val avatar: Bitmap?,
    val firstName: String?,
    val email: String,
    val webSite: String?
)
