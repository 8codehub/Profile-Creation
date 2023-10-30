package com.dev.profilecreation.domain.use_case

import com.dev.profilecreation.domain.repository.IRegistrationRepository
import javax.inject.Inject

class GetUser @Inject constructor(private val repository: IRegistrationRepository) {
    operator fun invoke(id: String) = repository.getUser(id)
}
