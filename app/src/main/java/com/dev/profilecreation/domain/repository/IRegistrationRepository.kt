package com.dev.profilecreation.domain.repository

import com.dev.profilecreation.data.remote.dto.User
import com.dev.profilecreation.data.remote.dto.UserInfo
import com.dev.profilecreation.data.remote.param.RegistrationParams
import kotlinx.coroutines.flow.Flow

interface IRegistrationRepository {
    fun submit(registrationParams: RegistrationParams): Flow<User>
    fun getUser(id: String): Flow<UserInfo?>
}
