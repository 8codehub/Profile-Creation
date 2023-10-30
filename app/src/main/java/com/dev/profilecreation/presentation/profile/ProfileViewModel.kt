package com.dev.profilecreation.presentation.profile

import com.dev.profilecreation.data.maper.userInfoDTOToProfileUserInfo
import com.dev.profilecreation.domain.model.ProfileUserInfo
import com.dev.profilecreation.domain.use_case.*
import com.dev.profilecreation.presentation.core.BaseViewModel
import com.dev.profilecreation.presentation.core.ButtonState
import com.dev.profilecreation.presentation.core.InputFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    val getUser: GetUser
) : BaseViewModel<ProfileScreenState, ProfileIntent, ProfileSingleEvent>() {
    override suspend fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.GetUserData -> {
                handleGetUserData(intent.input)
            }
            else                         -> Unit
        }
    }

    override fun reduceIntent(intent: ProfileIntent, state: ProfileScreenState) = when (intent) {
        is ProfileIntent.UserDataLoadingStarted -> state.copy(
            isBlockingLoading = true
        )

        is ProfileIntent.UserDataNotFound       -> state.copy(
            isBlockingLoading = false
        )

        is ProfileIntent.UserDataIsReady        -> state.copy(
            isBlockingLoading = false, profileUserInfo = intent.user
        )


        else                                    -> state
    }


    override fun initState() = ProfileScreenState(
        ProfileUserInfo(userId = "", avatar = null, firstName = null, email = "", webSite = ""), false
    )

    private suspend fun handleGetUserData(input: String) {
        sendIntent(ProfileIntent.UserDataLoadingStarted)
        getUser(input).collect {
            if (it != null) sendIntent(
                ProfileIntent.UserDataIsReady(userInfoDTOToProfileUserInfo(it))
            )
            else sendIntent(ProfileIntent.UserDataNotFound("User Not Found"))
        }
    }
}
