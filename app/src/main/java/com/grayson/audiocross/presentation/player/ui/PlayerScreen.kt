package com.grayson.audiocross.presentation.player.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.player.PlayerState
import com.grayson.audiocross.domain.player.PlayingState
import com.grayson.audiocross.presentation.albumlist.mapper.transformToTimeString
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCoverImage
import com.grayson.audiocross.presentation.navigator.ui.BackTopBar
import com.grayson.audiocross.presentation.player.viewmodel.PlayerViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = viewModel(),
    onNavigateUp: () -> Unit
) {

    LaunchedEffect(viewModel) {
        viewModel.startUpdateState()
    }

    val playerUiState: PlayerState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayScreenStateless(
        modifier = modifier,
        playerUiState = playerUiState,
        playerControlActions = PlayerControlActions(
            onPlayPress = viewModel::play,
            onPausePress = viewModel::pause,
            onAdvanceBy = viewModel::advanceBy,
            onRewindBy = viewModel::rewindBy,
            onNext = viewModel::next,
            onPrevious = viewModel::previous,
            onSeekingStarted = viewModel::seekStarted,
            onSeekingFinished = viewModel::seekFinished
        ),
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun PlayScreenStateless(
    modifier: Modifier = Modifier,
    playerUiState: PlayerState,
    playerControlActions: PlayerControlActions = PlayerControlActions(),
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(modifier = modifier, topBar = {
        BackTopBar(
            onNavigateUp = onNavigateUp
        )
    }) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            AlbumCoverImage(
                imageUrl = playerUiState.currentAudio?.work?.coverUrl ?: "",
                contentDescription = stringResource(id = R.string.cd_album_cover),
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = playerUiState.currentAudio?.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .basicMarquee()
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = playerUiState.currentAudio?.workTitle ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(10f)
            ) {
                PlayerSlider(
                    timeElapsed = playerUiState.currentPosition,
                    episodeDuration = playerUiState.currentAudio?.duration ?: 0L,
                    onSeekingStarted = playerControlActions.onSeekingStarted,
                    onSeekingFinished = playerControlActions.onSeekingFinished
                )
                PlayerButtons(
                    hasNext = playerUiState.playQueue.isNotEmpty(),
                    isPlaying = playerUiState.playingState == PlayingState.PLAYING,
                    onPlayPress = playerControlActions.onPlayPress,
                    onPausePress = playerControlActions.onPausePress,
                    onAdvanceBy = playerControlActions.onAdvanceBy,
                    onRewindBy = playerControlActions.onRewindBy,
                    onNext = playerControlActions.onNext,
                    onPrevious = playerControlActions.onPrevious,
                    Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun PlayerSlider(
    timeElapsed: Long,
    episodeDuration: Long?,
    onSeekingStarted: () -> Unit,
    onSeekingFinished: (newElapsed: Long) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        var sliderValue by remember(timeElapsed) { mutableLongStateOf(timeElapsed) }
        val maxRange = ((episodeDuration ?: 0)).toFloat()

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "${(sliderValue / 1000).formatString()} • ${episodeDuration?.formatString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Slider(value = (sliderValue / 1000).toFloat(), valueRange = 0f..maxRange, onValueChange = {
            onSeekingStarted()
            sliderValue = (it * 1000L).toLong()
        }, onValueChangeFinished = { onSeekingFinished(sliderValue) })
    }
}

fun Long.formatString(): String {
    return transformToTimeString(this)
}

@Composable
private fun PlayerButtons(
    hasNext: Boolean,
    isPlaying: Boolean,
    onPlayPress: () -> Unit,
    onPausePress: () -> Unit,
    onAdvanceBy: (Long) -> Unit,
    onRewindBy: (Long) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
    playerButtonSize: Dp = 72.dp,
    sideButtonSize: Dp = 48.dp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val sideButtonsModifier = Modifier
            .size(sideButtonSize)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = CircleShape
            )
            .semantics { role = Role.Button }

        val primaryButtonModifier = Modifier
            .size(playerButtonSize)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape
            )
            .semantics { role = Role.Button }

        Image(
            painter = painterResource(id = R.drawable.icon_skip_previous_24),
            contentDescription = stringResource(R.string.cd_skip_previous),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = sideButtonsModifier
                .clickable(enabled = isPlaying, onClick = onPrevious)
                .alpha(if (isPlaying) 1f else 0.25f)
        )
        Image(painter = painterResource(id = R.drawable.icon_replay_10_24),
            contentDescription = stringResource(R.string.cd_replay10),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = sideButtonsModifier.clickable {
                onRewindBy(10_000L)
            })
        if (isPlaying) {
            Image(painter = painterResource(id = R.drawable.icon_pause_24),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = primaryButtonModifier
                    .padding(8.dp)
                    .clickable {
                        onPausePress()
                    })
        } else {
            Image(imageVector = Icons.Outlined.PlayArrow,
                contentDescription = stringResource(R.string.cd_play),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = primaryButtonModifier
                    .padding(8.dp)
                    .clickable {
                        onPlayPress()
                    })
        }
        Image(painter = painterResource(id = R.drawable.icon_forward_10_24),
            contentDescription = stringResource(R.string.cd_forward10),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = sideButtonsModifier.clickable {
                onAdvanceBy(10_000L)
            })
        Image(
            painter = painterResource(id = R.drawable.icon_skip_next_24),
            contentDescription = stringResource(R.string.cd_skip_next),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = sideButtonsModifier
                .clickable(enabled = hasNext, onClick = onNext)
                .alpha(if (hasNext) 1f else 0.25f)
        )
    }
}


@Preview
@Composable
private fun PlayScreenPreview() {
    AudioCrossTheme {
        PlayScreenStateless(
            playerUiState = PlayerState()
        )
    }
}

/**
 * Wrapper around all actions for the player controls.
 */
data class PlayerControlActions(
    val onPlayPress: () -> Unit = {},
    val onPausePress: () -> Unit = {},
    val onAdvanceBy: (Long) -> Unit = {},
    val onRewindBy: (Long) -> Unit = {},
    val onNext: () -> Unit = {},
    val onPrevious: () -> Unit = {},
    val onSeekingStarted: () -> Unit = {},
    val onSeekingFinished: (newElapsed: Long) -> Unit = {}
)