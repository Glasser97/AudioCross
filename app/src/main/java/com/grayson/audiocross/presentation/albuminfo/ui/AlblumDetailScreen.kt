package com.grayson.audiocross.presentation.albuminfo.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.domain.player.IAudioPlayer
import com.grayson.audiocross.presentation.albuminfo.model.TrackDisplayItem
import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCoverImage
import com.grayson.audiocross.presentation.albumlist.ui.FailedScreen
import com.grayson.audiocross.presentation.navigator.AudioCrossNavActions
import com.grayson.audiocross.presentation.navigator.ui.BackTopBar
import com.grayson.audiocross.presentation.search.model.UiState
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent.inject

@Composable
fun AlbumDetailScreenStateless(
    modifier: Modifier = Modifier,
    albumCardDisplayItem: AlbumCardDisplayItem,
    trackList: List<TrackDisplayItem> = emptyList(),
    pageState: UiState = UiState.Init,
    onRefresh: () -> Unit = {},
    onClickAudio: (audio: TrackDisplayItem.TrackAudioDisplayItem, audioList: List<TrackDisplayItem.TrackAudioDisplayItem>) -> Unit = { _, _ -> },
    onClickText: (text: TrackDisplayItem.TrackTextDisplayItem) -> Unit = {},
    onClickFolder: (folder: TrackDisplayItem.TrackFolderDisplayItem) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {

    LaunchedEffect(true) {
        if (pageState == UiState.Init) {
            onRefresh()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackTopBar(
                    onNavigateUp = onNavigateUp
                )
                Text(
                    text = albumCardDisplayItem.albumCode,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceDim,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
    ) { innerPadding ->
        if (pageState is UiState.Error) {
            FailedScreen(
                modifier.padding(innerPadding),
                onRetry = { onRefresh() }
            )
        } else {
            val scrollState: ScrollState = rememberScrollState()
            Column(
                modifier = modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                    )
                    .verticalScroll(scrollState)
            ) {
                AlbumDetailCard(
                    albumCardDisplayItem = albumCardDisplayItem
                )
                trackList.forEach {
                    when (it) {
                        is TrackDisplayItem.TrackAudioDisplayItem -> {
                            TrackAudioItemStateLess(
                                audio = it,
                                audios = trackList.filterIsInstance<TrackDisplayItem.TrackAudioDisplayItem>(),
                                onClick = onClickAudio
                            )
                        }

                        is TrackDisplayItem.TrackTextDisplayItem -> {
                            TrackTextItemStateLess(
                                text = it,
                                onClick = onClickText
                            )
                        }

                        is TrackDisplayItem.TrackFolderDisplayItem -> {
                            TrackFolderItemStateLess(
                                folder = it,
                                onClickAudio = onClickAudio,
                                onClickText = onClickText,
                                onClick = onClickFolder
                            )
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun AlbumDetailScreen(
    actions: AudioCrossNavActions,
    modifier: Modifier = Modifier,
    viewModel: AlbumInfoViewModel,
    onNavigateUp: () -> Unit = {}
) {
    val displayItem by viewModel.albumInfo.collectAsStateWithLifecycle()
    val trackList by viewModel.albumTracks.collectAsStateWithLifecycle()
    val pageState by viewModel.pageState.collectAsStateWithLifecycle()
    val player: IAudioPlayer by remember { inject(IAudioPlayer::class.java) }

    AlbumDetailScreenStateless(
        modifier = modifier,
        albumCardDisplayItem = displayItem,
        trackList = trackList,
        pageState = pageState,
        onRefresh = { viewModel.fetchData() },
        onClickAudio = { audioDisplayItem, audioDisplayItems ->
            player.play(
                audioDisplayItems.map { it.domainData },
                audioDisplayItems.indexOf(audioDisplayItem).coerceIn(0, audioDisplayItems.size - 1)
            )
            actions.navigateToPlayer()
        },
        onClickText = { _ ->
            // todo text reader
        },
        onClickFolder = { folderDisplayItem ->
            folderDisplayItem.isExpanded.update { !it }
        },
        onNavigateUp = onNavigateUp
    )
}


@Composable
fun AlbumDetailCard(
    modifier: Modifier = Modifier,
    albumCardDisplayItem: AlbumCardDisplayItem
) {
    Column(
        modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            AlbumCoverImage(
                imageUrl = albumCardDisplayItem.coverUrl,
                contentDescription = albumCardDisplayItem.title,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = albumCardDisplayItem.duration,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp, bottom = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.BottomEnd),
            )

        }

        Text(
            text = albumCardDisplayItem.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = albumCardDisplayItem.voiceAuthor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.surfaceTint,
            style = MaterialTheme.typography.titleMedium
        )

    }

}

@Preview
@Composable
private fun AlbumDetailScreenPreview() {
    AudioCrossTheme {
        AlbumDetailScreenStateless(
            albumCardDisplayItem = AlbumCardDisplayItem(
                101L,
                "RJ101",
                "Title is too long,",
                "Voice Author",
                "CoverUrl",
                "2:00:00"
            )
        )
    }
}

@Preview
@Composable
private fun AlbumDetailCardPreview() {
    AudioCrossTheme {
        AlbumDetailCard(
            albumCardDisplayItem = AlbumCardDisplayItem(
                101L,
                "RJ101",
                "Title is too long,",
                "Voice Author",
                "CoverUrl",
                "2:00:00"
            )
        )
    }
}


