package com.grayson.audiocross.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCoverImage
import com.grayson.audiocross.presentation.navigator.model.PlayerBarState
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import androidx.compose.runtime.getValue
import com.grayson.audiocross.presentation.player.ui.CircularProgressButton
import com.grayson.audiocross.domain.player.PlayingState
import com.grayson.audiocross.presentation.player.viewmodel.PlayerViewModel

@Composable
fun PlayerBarStateful(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel,
    navigateToPlayer: () -> Unit
) {
    val playerBarState: PlayerBarState? by viewModel.playerBarState.collectAsStateWithLifecycle()

    PlayerBarStateless(
        modifier = modifier,
        playerBarState = playerBarState,
        navigateToPlayer = navigateToPlayer,
        onPlayOrPause = viewModel::pauseOrPlay
    )
}

@Composable
fun PlayerBarStateless(
    modifier: Modifier = Modifier,
    playerBarState: PlayerBarState?,
    navigateToPlayer: () -> Unit = {},
    onPlayOrPause: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                navigateToPlayer()
            }
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumCoverImage(
            imageUrl = playerBarState?.coverUrl ?: "",
            contentDescription = "Player bar album cover",
            modifier = Modifier
                .size(50.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = playerBarState?.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = playerBarState?.workTitle ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        CircularProgressButton(
            progress = playerBarState?.progress ?: 0f,
            onPlayPauseClicked = onPlayOrPause,
            isPlaying = playerBarState?.playingState == PlayingState.PLAYING,
            isEnabled = playerBarState?.playingState != PlayingState.BUFFERING
        )
    }
}

@Preview
@Composable
private fun PlayerBarPreview() {
    AudioCrossTheme {
        PlayerBarStateless(
            playerBarState = PlayerBarState(
                title = "This is title",
                workTitle = "This is work title",
                coverUrl = "",
                progress = 0.25f,
                playingState = PlayingState.PLAYING
            )
        )
    }
}