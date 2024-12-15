package com.grayson.audiocross.presentation.navigator

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCardListScreen
import com.grayson.audiocross.presentation.login.ui.LoginScreen
import androidx.navigation.NavHostController
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.presentation.albuminfo.ui.AlbumDetailScreen
import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.player.ui.PlayerScreen
import com.grayson.audiocross.presentation.search.ui.SearchScreen
import org.koin.android.compat.ViewModelCompat.getViewModel
import org.koin.core.parameter.parametersOf

const val TRANSITION_TIME = 350

@Composable
fun AudioCrossGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    audioCrossNavActions: AudioCrossNavActions,
) {
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
                navigatorToSearch = audioCrossNavActions.navigateToSearch,
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

        composable(AudioCrossDestinations.PLAYER_ROUTE,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(TRANSITION_TIME)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(TRANSITION_TIME)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(TRANSITION_TIME)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(TRANSITION_TIME)
                )
            },
            ) {
            PlayerScreen(
                onNavigateUp = audioCrossNavActions.navigateUp
            )
        }

        composable(AudioCrossDestinations.SEARCH_ROUTE) {
            SearchScreen(
                navigatorToDetail = { albumCardDisplayItem ->
                    audioCrossNavActions.navigateToDetail(albumCardDisplayItem.albumId)
                },
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
    const val SEARCH_ROUTE = "search_route"

    private val requiredLoginRoutes = setOf(
        FAVORITE_ROUTE
    )

    fun requireLogin(route: String, userInfo: User? = null): Boolean {
        return requiredLoginRoutes.contains(route) &&
                (userInfo?.username.isNullOrBlank() || userInfo?.token.isNullOrBlank())
    }
}