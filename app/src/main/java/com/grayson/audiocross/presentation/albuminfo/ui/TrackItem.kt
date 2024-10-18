package com.grayson.audiocross.presentation.albuminfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.albuminfo.model.TrackItem
import com.grayson.audiocross.presentation.albuminfo.model.TrackDisplayItem
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import kotlinx.coroutines.flow.update

// region composable

@Composable
fun TrackAudioItemStateLess(
    modifier: Modifier = Modifier,
    audio: TrackDisplayItem.TrackAudioDisplayItem,
    onClick: (audio: TrackDisplayItem.TrackAudioDisplayItem) -> Unit = {},
) {
    Row(modifier = modifier
        .wrapContentHeight()
        .clickable {
            onClick(audio)
        }
        .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        val isPlaying by audio.isPlaying.collectAsStateWithLifecycle()

        val iconId = if (isPlaying) {
            R.drawable.icon_pause_24
        } else {
            R.drawable.icon_play_arrow_24
        }

        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = audio.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = audio.duration,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }


    }
}

@Composable
fun TrackTextItemStateLess(
    modifier: Modifier = Modifier,
    text: TrackDisplayItem.TrackTextDisplayItem,
    onClick: (text: TrackDisplayItem.TrackTextDisplayItem) -> Unit = {},
) {
    Row(modifier = modifier
        .wrapContentHeight()
        .clickable {
            onClick(text)
        }
        .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.icon_text_snippet_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = text.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TrackFolderItemStateLess(
    modifier: Modifier = Modifier,
    folder: TrackDisplayItem.TrackFolderDisplayItem,
    onClickAudio: (audio: TrackDisplayItem.TrackAudioDisplayItem) -> Unit = {},
    onClickText: (text: TrackDisplayItem.TrackTextDisplayItem) -> Unit = {},
    onClick: (folder: TrackDisplayItem.TrackFolderDisplayItem) -> Unit = {},
) {
    val isExpanded by folder.isExpanded.collectAsStateWithLifecycle()

    Column {
        Row(modifier = modifier
            .wrapContentHeight()
            .clickable {
                onClick(folder)
            }
            .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            val iconId = if (isExpanded) {
                R.drawable.icon_folder_open_24
            } else {
                R.drawable.icon_folder_24
            }

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = folder.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        if (isExpanded) {
            folder.children.forEach {
                when (it) {
                    is TrackDisplayItem.TrackAudioDisplayItem -> {
                        TrackAudioItemStateLess(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceDim
                                )
                                .padding(start = 8.dp),
                            audio = it,
                            onClick = onClickAudio
                        )
                    }

                    is TrackDisplayItem.TrackTextDisplayItem -> {
                        TrackTextItemStateLess(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceDim
                                )
                                .padding(start = 8.dp),
                            text = it,
                            onClick = onClickText
                        )
                    }

                    is TrackDisplayItem.TrackFolderDisplayItem -> {
                        TrackFolderItemStateLess(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceDim
                                )
                                .padding(start = 8.dp),
                            folder = it,
                            onClickAudio = onClickAudio,
                            onClickText = onClickText,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }


}

// endregion

// region preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TrackAudioItemPreview() {
    AudioCrossTheme {
        TrackAudioItemStateLess(audio = TrackDisplayItem.TrackAudioDisplayItem(
            domainData = TrackItem.Audio(
                title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                duration = 1120000L,
                fileSize = 0L,
                streamUrl = "test url",
                downloadUrl = "test url",
                hash = "test url",
                streamLowQualityUrl = "test url",
                work = null,
                workTitle = "test url"
            ),
            title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
            hash = "",
            albumTitle = "",
            duration = "11:20:00",
            size = 0L,
            streamUrl = "test url",
            downloadUrl = "test url",
            downsizeStreamUrl = "test url"
        ), onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TrackTextItemPreview() {
    AudioCrossTheme {
        TrackTextItemStateLess(text = TrackDisplayItem.TrackTextDisplayItem(
            domainData = TrackItem.Text(
                title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                duration = 1120000L,
                fileSize = 0L,
                streamUrl = "test url",
                downloadUrl = "test url",
                hash = "test url",
                work = null,
                workTitle = "test url"
            ),
            title = "This is Text Title. This is Text Title. This is Text Title. This is Text Title.",
            hash = "",
            albumTitle = "",
            size = 0L,
            streamUrl = "test url",
            downloadUrl = "test url",
        ), onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TrackFolderItemPreview() {
    AudioCrossTheme {
        TrackFolderItemStateLess(folder = TrackDisplayItem.TrackFolderDisplayItem(
            domainData = TrackItem.Folder(
                title = "This is Folder Title. This is Folder Title.",
                children = emptyList()
            ),
            title = "This is Folder Title. This is Folder Title.",
            children = listOf(
                TrackDisplayItem.TrackTextDisplayItem(
                    domainData = TrackItem.Text(
                        title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                        duration = 1120000L,
                        fileSize = 0L,
                        streamUrl = "test url",
                        downloadUrl = "test url",
                        hash = "test url",
                        work = null,
                        workTitle = "test url"
                    ),
                    title = "This is Text Title. This is Text Title. This is Text Title. This is Text Title.",
                    hash = "",
                    albumTitle = "",
                    size = 0L,
                    streamUrl = "test url",
                    downloadUrl = "test url",
                ),
                TrackDisplayItem.TrackAudioDisplayItem(
                    domainData = TrackItem.Audio(
                        title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                        duration = 1120000L,
                        fileSize = 0L,
                        streamUrl = "test url",
                        downloadUrl = "test url",
                        hash = "test url",
                        work = null,
                        workTitle = "test url",
                        streamLowQualityUrl = "test url"
                    ),
                    title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                    hash = "",
                    albumTitle = "",
                    duration = "11:20:00",
                    size = 0L,
                    streamUrl = "test url",
                    downloadUrl = "test url",
                    downsizeStreamUrl = "test url"
                ),

                TrackDisplayItem.TrackFolderDisplayItem(
                    domainData = TrackItem.Folder(
                        title = "This is Folder Title. This is Folder Title.",
                        children = emptyList()
                    ),
                    title = "This is Folder Title. This is Folder Title.",
                    children = listOf(
                        TrackDisplayItem.TrackTextDisplayItem(
                            domainData = TrackItem.Text(
                                title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                                duration = 1120000L,
                                fileSize = 0L,
                                streamUrl = "test url",
                                downloadUrl = "test url",
                                hash = "test url",
                                work = null,
                                workTitle = "test url"
                            ),
                            title = "This is Text Title. This is Text Title. This is Text Title. This is Text Title.",
                            hash = "",
                            albumTitle = "",
                            size = 0L,
                            streamUrl = "test url",
                            downloadUrl = "test url",
                        ),

                        TrackDisplayItem.TrackAudioDisplayItem(
                            domainData = TrackItem.Audio(
                                title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                                duration = 1120000L,
                                fileSize = 0L,
                                streamUrl = "test url",
                                downloadUrl = "test url",
                                hash = "test url",
                                work = null,
                                workTitle = "test url",
                                streamLowQualityUrl = "test url"
                            ),
                            title = "This is Audio Title. This is Audio Title. This is Audio Title. This is Audio Title.",
                            hash = "",
                            albumTitle = "",
                            duration = "11:20:00",
                            size = 0L,
                            streamUrl = "test url",
                            downloadUrl = "test url",
                            downsizeStreamUrl = "test url"
                        )
                    )
                ).apply {
                    this.isExpanded.update { true }
                }
            )
        ).apply {
            this.isExpanded.value = true
        }, onClick = {})
    }
}

// endregion