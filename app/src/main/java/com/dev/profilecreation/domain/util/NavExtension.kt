package com.dev.profilecreation.domain.util

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(directions: NavDirections) {
    val actionId = directions.actionId
    val action = currentDestination?.getAction(actionId)
    if (action != null) {
        navigate(directions)
    } else {
        Log.e("NavController", "Navigation action/destination $actionId cannot be found from the current destination.")
    }
}
