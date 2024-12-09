package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.TextField
import androidx.compose.material3.IconButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.domain.login.model.isLogin
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.viewmodel.AlbumListViewModel
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumCardListScreenViewModeless(
    modifier: Modifier = Modifier,
    albumCardDisplayItems: List<AlbumCardDisplayItem>,
    userInfo: User? = null,
    searchKeyWords: String = "",
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    onSearchTextChanged: (String) -> Unit = {},
    refreshAlbumList: () -> Unit = {},
    loadMoreAlbumList: () -> Unit = {},
    navigatorToPlayer: (AlbumCardDisplayItem) -> Unit = {},
    onClickSearch: () -> Unit = {},
    navigatorToLogin: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        refreshAlbumList()
    })
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                userInfo = userInfo,
                onClickUserItem = {
                    if (userInfo.isLogin()) {
                        onLogout()
                    } else {
                        navigatorToLogin()
                    }
                }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier, topBar = {
                AlbumListTopBar(
                    searchKeyWords = searchKeyWords,
                    onSearchTextChanged = onSearchTextChanged,
                    onClickMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onClickSearch = onClickSearch
                )
            }, containerColor = MaterialTheme.colorScheme.surface
        ) { padding ->
            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    .padding(padding)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(albumCardDisplayItems,
                        key = { _, item -> item.albumId }) { index, albumItem ->
                        AlbumCard(
                            albumCardDisplayItem = albumItem,
                            onClick = { navigatorToPlayer(it) })
                        if (index >= albumCardDisplayItems.size - 2 && !isLoadingMore) {
                            loadMoreAlbumList()
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing, state = pullRefreshState, Modifier.align(
                        Alignment.TopCenter
                    )
                )
            }
        }
    }
}

/**
 * Album Card List
 */
@Composable
fun AlbumCardListScreen(
    modifier: Modifier = Modifier,
    navigatorToDetail: (AlbumCardDisplayItem) -> Unit = {},
    navigatorToSearch: () -> Unit = {},
    navigatorToLogin: () -> Unit,
    listViewModel: AlbumListViewModel = viewModel(),
    mainViewModel: AudioCrossViewModel = viewModel()
) {
    val isRefreshing by listViewModel.isRefreshing.collectAsStateWithLifecycle()
    val isLoadingMore by listViewModel.isLoadingMore.collectAsStateWithLifecycle()
    val albumCardDisplayItems: List<AlbumCardDisplayItem> by listViewModel.albumList.collectAsStateWithLifecycle()
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val filterParam by listViewModel.filterParam.collectAsStateWithLifecycle()
    AlbumCardListScreenViewModeless(
        modifier = modifier,
        navigatorToPlayer = navigatorToDetail,
        albumCardDisplayItems = albumCardDisplayItems,
        userInfo = uiState.user,
        searchKeyWords = filterParam.keywords ?: "",
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        onSearchTextChanged = { text ->
            listViewModel.updateKeywords(text)
        },
        refreshAlbumList = { listViewModel.refreshAlbumList() },
        loadMoreAlbumList = { listViewModel.loadMoreAlbumList() },
        onClickSearch = navigatorToSearch,
        navigatorToLogin = navigatorToLogin,
        onLogout = {
            mainViewModel.onLogout()
        }
    )
}

@Composable
fun AlbumListTopBar(
    modifier: Modifier = Modifier,
    searchKeyWords: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClickMenu: () -> Unit = {},
    onClickSearch: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onClickMenu) {
            Icon(
                painter = painterResource(id = R.drawable.icon_menu_open_24),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = searchKeyWords,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            ),
            trailingIcon = {
                IconButton(onClick = onClickSearch) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_search_24),
                        contentDescription = null
                    )
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onClickSearch) {
            Icon(
                painter = painterResource(id = R.drawable.icon_favorite_border_24),
                contentDescription = null
            )
        }

    }
}

@Preview
@Composable
private fun AlbumListTopBarPreview() {
    AudioCrossTheme {
        AlbumListTopBar(
            searchKeyWords = "RJ101",
            onClickMenu = {},
            onClickSearch = {}
        )
    }
}

@Composable
@Preview
fun AlbumCardListPreview() {
    AudioCrossTheme {
        AlbumCardListScreenViewModeless(
            searchKeyWords = "RJ101",
            albumCardDisplayItems = listOf(
                AlbumCardDisplayItem(
                    101L,
                    "RJ101", "Title", "Voice Author", "CoverUrl", "2:00:00"
                ), AlbumCardDisplayItem(
                    102L,
                    "RJ102",
                    "Title is too long, Title is too long, Title is too long, Title is too long, " + "Title is too long, Title is too long, Title is too long, ",
                    "Voice Author",
                    "CoverUrl",
                    "2:00:00"
                )
            )
        )
    }
}