package com.dev.profilecreation.data.maper

import com.dev.profilecreation.data.remote.dto.UserInfo
import com.dev.profilecreation.data.remote.param.RegistrationParams
import com.dev.profilecreation.domain.model.ProfileUserInfo

fun registrationParamsToUserInfo(params: RegistrationParams, userId: String) = UserInfo(
    userId = userId,
    avatar = params.avatar,
    firstName = params.firstName,
    email = params.email,
    webSite = params.webSite
)

fun userInfoDTOToProfileUserInfo(info: UserInfo) = ProfileUserInfo(
    userId = info.userId,
    avatar = info.avatar,
    firstName = info.firstName,
    email = info.email,
    webSite = info.webSite
)