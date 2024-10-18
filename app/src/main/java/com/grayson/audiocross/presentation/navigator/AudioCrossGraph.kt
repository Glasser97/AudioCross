package com.grayson.audiocross.presentation.navigator

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCardListScreen
import com.grayson.audiocross.presentation.login.ui.LoginScreen
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.presentation.albuminfo.ui.AlbumDetailScreen
import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.player.ui.PlayerScreen
import com.grayson.audiocross.presentation.player.viewmodel.PlayerViewModel
import org.koin.android.compat.ViewModelCompat.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

const val TRANSITION_TIME = 350

@Composable
fun AudioCrossGraph(
    modifier: Modifier = Modifier,
    viewModel: AudioCrossViewModel = viewModel(),
    activity: ComponentActivity
) {
    // 创建 navController
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val audioCrossNavActions = remember(navController, uiState.user) {
        AudioCrossNavActions(navController, uiState.user)
    }

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = AudioCrossDestinations.ALBUM_LIST_ROUTE,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_TIME)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(TRANSITION_TIME)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_TIME)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(TRANSITION_TIME)
            )
        }
    ) {
        composable(AudioCrossDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onNavigateUp = audioCrossNavActions.navigateUp,
                onPopBackStackToMain = { audioCrossNavActions.popBackStack(AudioCrossDestinations.ALBUM_LIST_ROUTE) }
            )
        }
        composable(AudioCrossDestinations.ALBUM_LIST_ROUTE) {
            AlbumCardListScreen(
                navigatorToLogin = audioCrossNavActions.navigateToLogin,
                navigatorToDetail = { albumCardDisplayItem ->
                    audioCrossNavActions.navigateToDetail(albumCardDisplayItem.albumId)
                }
            )
        }
        composable(AudioCrossDestinations.ALBUM_INFO_ROUTE + "/{albumId}") { navBackStackEntry ->
            // 获取参数
            val albumId = navBackStackEntry.arguments?.getString("albumId") ?: return@composable
            val owner = LocalViewModelStoreOwner.current ?: return@composable
            val albumInfoViewModel: AlbumInfoViewModel =
                getViewModel(owner = owner,
                    clazz = AlbumInfoViewModel::class.java,
                    parameters = {
                        parametersOf(albumId.toLong())
                    })
            AlbumDetailScreen(
                actions = audioCrossNavActions,
                viewModel = albumInfoViewModel,
                onNavigateUp = audioCrossNavActions.navigateUp
            )
        }

        composable(AudioCrossDestinations.PLAYER_ROUTE) {
            val player: IAudioPlayer by remember { inject(IAudioPlayer::class.java) }
            val playerViewModel = PlayerViewModel(audioPlayer = player)
            PlayerScreen(
                viewModel = playerViewModel,
                onNavigateUp = audioCrossNavActions.navigateUp
            )
        }
    }
}

object AudioCrossDestinations {
    const val LOGIN_ROUTE = "login_route"
    const val ALBUM_LIST_ROUTE = "album_list_route"
    const val FAVORITE_ROUTE = "favorite_route"
    const val ALBUM_INFO_ROUTE = "album_info_route"
    const val PLAYER_ROUTE = "player_route"

    private val requiredLoginRoutes = setOf(
        FAVORITE_ROUTE
    )

    fun requireLogin(route: String, userInfo: User? = null): Boolean {
        return requiredLoginRoutes.contains(route) &&
                (userInfo?.username.isNullOrBlank() || userInfo?.token.isNullOrBlank())
    }
}