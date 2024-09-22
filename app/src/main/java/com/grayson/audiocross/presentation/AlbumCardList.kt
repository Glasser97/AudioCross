package com.grayson.audiocross.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grayson.audiocross.ui.theme.AudioCrossTheme

/**
 * Album Card List
 */
@Composable
fun AlbumCardList(
    modifier: Modifier = Modifier,
    albumCardItems: List<AlbumCardItem>,
    navigatorToPlayer: (AlbumCardItem) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(albumCardItems, key = { it.albumId }) { albumItem ->
            AlbumCard(
                albumCardItem = albumItem,
                onClick = { navigatorToPlayer(it) }
            )
        }
    }
}

@Composable
@Preview
fun AlbumCardListPreview() {
    AudioCrossTheme {
        AlbumCardList(
            albumCardItems = listOf(
                AlbumCardItem(
                    101L,
                    "Title",
                    "Voice Author",
                    "CoverUrl",
                    "2:00:00"
                ),
                AlbumCardItem(
                    102L,
                    "Title is too long, Title is too long, Title is too long, Title is too long, " +
                            "Title is too long, Title is too long, Title is too long, ",
                    "Voice Author",
                    "CoverUrl",
                    "2:00:00"
                )
            )
        )
    }
}