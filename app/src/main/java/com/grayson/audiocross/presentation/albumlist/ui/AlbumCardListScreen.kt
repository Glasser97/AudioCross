package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.grayson.audiocross.R
import com.grayson.audiocross.domain.login.model.User
import com.grayson.audiocross.domain.login.model.isLogin
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.viewmodel.AlbumListViewModel
import com.grayson.audiocross.presentation.navigator.viewmodel.AudioCrossViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumCardListScreenViewModeless(
    modifier: Modifier = Modifier,
    albumCardDisplayItems: LazyPagingItems<AlbumCardDisplayItem>,
    userInfo: User? = null,
    isRefreshing: Boolean = false,
    refreshAlbumList: () -> Unit = {},
    navigatorToPlayer: (AlbumCardDisplayItem) -> Unit = {},
    navigatorToSearch: () -> Unit = {},
    navigatorToLogin: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
                    onClickMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onClickSearch = navigatorToSearch
                )
            }, containerColor = MaterialTheme.colorScheme.surface
        ) { padding ->

            val pullRefreshState = rememberPullRefreshState(isRefreshing, {
                refreshAlbumList()
            })
            Box(
                modifier
                    .pullRefresh(pullRefreshState)
                    .padding(padding)
            ) {
                when (albumCardDisplayItems.loadState.refresh) {
                    is LoadState.Error -> {
                        FailedScreen(onRetry = { refreshAlbumList() })
                    }

                    is LoadState.Loading, is LoadState.NotLoading -> {
                        if (albumCardDisplayItems.itemCount == 0 && albumCardDisplayItems.loadState.refresh is LoadState.NotLoading) {
                            EmptyScreen()
                        } else {
                            AlbumListColumn(
                                albumCardDisplayItems = albumCardDisplayItems,
                                navigatorToPlayer = navigatorToPlayer
                            )
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

@Composable
fun AlbumListColumn(
    modifier: Modifier = Modifier,
    albumCardDisplayItems: LazyPagingItems<AlbumCardDisplayItem>,
    navigatorToPlayer: (AlbumCardDisplayItem) -> Unit = {},
) {

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(albumCardDisplayItems.itemCount,
            key = { index ->
                albumCardDisplayItems[index]?.albumId ?: -1L
            }) { index ->
            albumCardDisplayItems[index]?.let {
                AlbumCard(
                    albumCardDisplayItem = it,
                    onClick = { navigatorToPlayer(it) })
            }
        }

        // Footer
        albumCardDisplayItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item {
                        LoadMoreFooter()
                    }
                }

                is LoadState.Error -> {
                    item {
                        LoadMoreFooter(
                            needRetry = true,
                            retry = { retry() }
                        )
                    }
                }

                else -> Unit
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
    listViewModel: AlbumListViewModel = koinViewModel<AlbumListViewModel>(),
    mainViewModel: AudioCrossViewModel = koinViewModel<AudioCrossViewModel>()
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = listViewModel.pagingDataFlow.collectAsLazyPagingItems()
    val isRefreshing = pagingItems.loadState.refresh == LoadState.Loading
    AlbumCardListScreenViewModeless(
        modifier = modifier,
        navigatorToPlayer = navigatorToDetail,
        albumCardDisplayItems = pagingItems,
        userInfo = uiState.user,
        isRefreshing = isRefreshing,
        refreshAlbumList = { pagingItems.refresh() },
        navigatorToSearch = navigatorToSearch,
        navigatorToLogin = navigatorToLogin,
        onLogout = {
            mainViewModel.onLogout()
        }
    )
}

@Composable
fun AlbumListTopBar(
    modifier: Modifier = Modifier,
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

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onClickSearch) {
            Icon(
                painter = painterResource(id = R.drawable.icon_search_24),
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
            onClickMenu = {},
            onClickSearch = {}
        )
    }
}