package com.grayson.audiocross.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grayson.audiocross.ui.theme.AudioCrossTheme

/**
 * Album Card List
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumCardList(
    modifier: Modifier = Modifier,
    albumCardDisplayItems: List<AlbumCardDisplayItem>,
    navigatorToPlayer: (AlbumCardDisplayItem) -> Unit = {},
    refreshCallback: () -> Unit = {},
    isRefreshing: Boolean = false
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        refreshCallback()
    })

    Box(modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(albumCardDisplayItems, key = { it.albumId }) { albumItem ->
                AlbumCard(
                    albumCardDisplayItem = albumItem,
                    onClick = { navigatorToPlayer(it) }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing, state = pullRefreshState, Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}

@Composable
@Preview
fun AlbumCardListPreview() {
    AudioCrossTheme {
        AlbumCardList(
            albumCardDisplayItems = listOf(
                AlbumCardDisplayItem(
                    101L,
                    "Title",
                    "Voice Author",
                    "CoverUrl",
                    "2:00:00"
                ),
                AlbumCardDisplayItem(
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