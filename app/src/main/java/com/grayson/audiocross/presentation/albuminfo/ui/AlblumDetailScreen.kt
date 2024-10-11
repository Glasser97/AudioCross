package com.grayson.audiocross.presentation.albuminfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.presentation.albuminfo.viewmodel.AlbumInfoViewModel
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCoverImage
import com.grayson.audiocross.presentation.navigator.ui.BackTopBar
import com.grayson.audiocross.ui.theme.AudioCrossTheme

@Composable
fun AlbumDetailScreenStateless(
    modifier: Modifier = Modifier,
    albumCardDisplayItem: AlbumCardDisplayItem,
    onNavigateUp: () -> Unit = {}
) {

    Column(
        modifier = modifier
    ) {
        AlbumDetailCard(
            albumCardDisplayItem = albumCardDisplayItem,
            onNavigateUp = onNavigateUp
        )
    }

}

@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumInfoViewModel,
    onNavigateUp: () -> Unit = {}
) {
    val displayItem by viewModel.albumInfo.collectAsStateWithLifecycle()

    AlbumDetailScreenStateless(
        modifier = modifier,
        albumCardDisplayItem = displayItem,
        onNavigateUp = onNavigateUp
    )
}


@Composable
fun AlbumDetailCard(
    modifier: Modifier = Modifier,
    albumCardDisplayItem: AlbumCardDisplayItem,
    onNavigateUp: () -> Unit = {}
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
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackTopBar(
                    onNavigateUp = onNavigateUp
                )
                Text(
                    text = albumCardDisplayItem.albumId.toString(),
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
            maxLines = 3,
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
                "Title is too long,",
                "Voice Author",
                "CoverUrl",
                "2:00:00"
            )
        )
    }
}


