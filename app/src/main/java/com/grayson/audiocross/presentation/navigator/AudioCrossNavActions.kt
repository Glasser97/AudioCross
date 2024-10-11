package com.grayson.audiocross.presentation.navigator

import androidx.navigation.NavHostController
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.presentation.navigator.AudioCrossDestinations.requireLogin

class AudioCrossNavActions(
    private val navController: NavHostController,
    private val user: User?
) {

    val navigateToLogin: () -> Unit = {
        navigate(AudioCrossDestinations.LOGIN_ROUTE)
    }

    val navigateToDetail: (id: Long) -> Unit = { id ->
        navigate(AudioCrossDestinations.ALBUM_INFO_ROUTE + "/${id}")
    }

    val popBackStack: (route: String) -> Unit = {
        navController.popBackStack(it, false)
    }

    val navigateUp: () -> Unit = {
        if (!navController.navigateUp()) {
            navigate(AudioCrossDestinations.ALBUM_LIST_ROUTE)
        }
    }


    fun navigate(route: String) {
        navController.graph.findNode(route) ?: return
        if (requireLogin(route)) {
            navController.navigate(AudioCrossDestinations.LOGIN_ROUTE)
        } else {
            navController.navigate(route)
        }
    }
}