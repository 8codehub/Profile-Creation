package com.dev.profilecreation.domain.service

import com.dev.profilecreation.data.remote.dto.User
import com.dev.profilecreation.data.remote.dto.UserInfo
import com.dev.profilecreation.data.remote.param.RegistrationParams

interface NetworkService {
    suspend fun submitRequest(params: RegistrationParams): User
    suspend fun getUser(id: String): UserInfo?
}
