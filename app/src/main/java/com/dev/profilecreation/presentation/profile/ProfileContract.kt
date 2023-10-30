package com.dev.profilecreation.presentation.profile

import android.graphics.Bitmap
import com.dev.profilecreation.domain.model.ProfileUserInfo
import com.dev.profilecreation.presentation.core.*

data class ProfileScreenState(
    val profileUserInfo: ProfileUserInfo,
    val isBlockingLoading: Boolean
) : State

sealed interface ProfileIntent : Intent {
    data class GetUserData(val input: String) : ProfileIntent
    object UserDataLoadingStarted : ProfileIntent
    data class UserDataIsReady(val user: ProfileUserInfo) : ProfileIntent
    data class UserDataNotFound(val message: String) : ProfileIntent

}

sealed interface ProfileSingleEvent : SingleEvent {
    data class NavigateSignIn(val id: String) : ProfileSingleEvent
}
