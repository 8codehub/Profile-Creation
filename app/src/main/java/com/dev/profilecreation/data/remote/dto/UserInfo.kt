package com.dev.profilecreation.data.remote.dto

import android.graphics.Bitmap

data class UserInfo(
    val userId: String,
    val avatar: Bitmap?,
    val firstName: String?,
    val email: String,
    val webSite: String?
)
