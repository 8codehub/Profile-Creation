package com.dev.profilecreation.data.remote.service

import com.dev.profilecreation.data.maper.registrationParamsToUserInfo
import com.dev.profilecreation.data.remote.dto.User
import com.dev.profilecreation.data.remote.dto.UserInfo
import com.dev.profilecreation.data.remote.param.RegistrationParams
import com.dev.profilecreation.domain.service.NetworkService
import kotlinx.coroutines.delay
import java.util.UUID

class FakeNetworkService : NetworkService {

    private val fakeData = mutableMapOf<String, UserInfo>()
    override suspend fun submitRequest(params: RegistrationParams): User {
        delay(2000)
        val userId = UUID.randomUUID().toString()
        val userInfo = registrationParamsToUserInfo(params, userId)
        fakeData[userId] = userInfo
        return User(userId)
    }

    override suspend fun getUser(id: String): UserInfo? {
        delay(2000)
        return fakeData[id]
    }
}
