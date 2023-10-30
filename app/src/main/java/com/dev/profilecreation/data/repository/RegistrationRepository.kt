package com.dev.profilecreation.data.repository

import com.dev.profilecreation.data.remote.dto.User
import com.dev.profilecreation.data.remote.dto.UserInfo
import com.dev.profilecreation.data.remote.param.RegistrationParams
import com.dev.profilecreation.data.remote.service.FakeNetworkService
import com.dev.profilecreation.domain.repository.IRegistrationRepository
import com.dev.profilecreation.domain.service.NetworkService
import kotlinx.coroutines.flow.flow

class RegistrationRepository(private val networkService: NetworkService) : IRegistrationRepository {
    override fun submit(registrationParams: RegistrationParams) = flow { emit(networkService.submitRequest(registrationParams)) }
    override fun getUser(id: String)=  flow { emit(networkService.getUser(id)) }
}
