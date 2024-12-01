package com.grayson.audiocross

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.presentation.navigator.AudioCrossDestinations
import com.grayson.audiocross.presentation.navigator.AudioCrossGraph
import com.grayson.audiocross.presentation.navigator.AudioCrossNavActions
import com.grayson.audiocross.presentation.navigator.model.PlayerBarState
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import com.grayson.audiocross.presentation.player.PlayerBarStateful
import com.grayson.audiocross.presentation.player.viewmodel.PlayerViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    // region field

    private val userInfoHelper: IUserInfoHelper by inject(IUserInfoHelper::class.java)

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioCrossTheme {

                // 创建 navController
                val mainViewModel: AudioCrossViewModel = viewModel()
                val playerViewModel: PlayerViewModel = viewModel()
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
                val playerBarState: PlayerBarState? by playerViewModel.playerBarState.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                val audioCrossNavActions = remember(navController, uiState.user) {
                    AudioCrossNavActions(navController, uiState.user)
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        val isBottomBarVisible = !playerBarState?.title.isNullOrEmpty()
                                && currentRoute != AudioCrossDestinations.PLAYER_ROUTE
                        AnimatedVisibility(
                            visible = isBottomBarVisible,
                            enter = slideInVertically(initialOffsetY = { it }), // 从底部进入
                            exit = slideOutVertically(targetOffsetY = { it }) // 向底部退出
                        ) {
                            PlayerBarStateful(
                                viewModel = playerViewModel,
                                navigateToPlayer = {
                                    audioCrossNavActions.navigateToPlayer()
                                }
                            )
                        }
                    }) { padding ->
                    AudioCrossGraph(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        audioCrossNavActions = audioCrossNavActions
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userInfoHelper.close()
    }
}