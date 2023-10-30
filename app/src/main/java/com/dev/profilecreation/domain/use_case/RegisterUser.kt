package com.dev.profilecreation.domain.use_case

import com.dev.profilecreation.data.remote.param.RegistrationParams
import com.dev.profilecreation.domain.repository.IRegistrationRepository
import javax.inject.Inject

class RegisterUser @Inject constructor(private val repository: IRegistrationRepository) {
    operator fun invoke(params: RegistrationParams) = repository.submit(params)
}
