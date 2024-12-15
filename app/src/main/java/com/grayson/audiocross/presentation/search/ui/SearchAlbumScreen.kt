package com.grayson.audiocross.presentation.search.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grayson.audiocross.R
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam
import com.grayson.audiocross.presentation.albumlist.ui.AlbumCard
import com.grayson.audiocross.presentation.search.model.FilterUIItem
import com.grayson.audiocross.presentation.search.viewmodel.SearchViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navigatorToDetail: (AlbumCardDisplayItem) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    searchViewModel: SearchViewModel = viewModel()
) {

    val isRefreshing by searchViewModel.isRefreshing.collectAsStateWithLifecycle()
    val isLoadingMore by searchViewModel.isLoadingMore.collectAsStateWithLifecycle()
    val albumCardDisplayItems: List<AlbumCardDisplayItem> by searchViewModel.albumList.collectAsStateWithLifecycle()
    val filterParam by searchViewModel.filterParam.collectAsStateWithLifecycle()
    val searchKeywords by searchViewModel.keywords.collectAsStateWithLifecycle()

    SearchScreenViewModeless(
        modifier = modifier,
        navigatorToPlayer = navigatorToDetail,
        albumCardDisplayItems = albumCardDisplayItems,
        filterParam = filterParam,
        searchKeywords = searchKeywords,
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        refreshAlbumList = { searchViewModel.refreshAlbumList() },
        loadMoreAlbumList = { searchViewModel.loadMoreAlbumList() },
        onUpdateFilterParam = { filter -> searchViewModel.updateFilterParam(filter)},
        onSearchTextChanged = { keywords -> searchViewModel.updateKeywords(keywords)},
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreenViewModeless(
    modifier: Modifier = Modifier,
    albumCardDisplayItems: List<AlbumCardDisplayItem>,
    filterParam: AlbumListFilterParam? = null,
    searchKeywords: String? = null,
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    refreshAlbumList: () -> Unit = {},
    loadMoreAlbumList: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    onUpdateFilterParam: (AlbumListFilterParam) -> Unit = {},
    navigatorToPlayer: (AlbumCardDisplayItem) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        refreshAlbumList()
    })
    Scaffold(
        modifier = modifier, topBar = {
            SearchTopBar(
                searchKeyWords = searchKeywords ?: "",
                onSearchTextChanged = onSearchTextChanged,
                onClickSearch = {
                    refreshAlbumList()
                },
                filterWindow = { isPopupVisible, iconPosition, onDismissRequest ->
                    if (filterParam != null) {
                        DropdownMenu(
                            expanded = isPopupVisible,
                            onDismissRequest = onDismissRequest,
                            offset = DpOffset(iconPosition.x.dp, 0.dp)
                        ) {
                            FilterWindow(modifier = Modifier,
                                filterParam = filterParam,
                                filterUIItems = FilterUIItem.filterUIItems,
                                onUpdateFilterParam = onUpdateFilterParam)
                        }
                    }
                },
                onNavigateUp = onNavigateUp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    searchKeyWords: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClickSearch: () -> Unit = {},
    filterWindow: @Composable (Boolean, IntOffset, () -> Unit) -> Unit = {_, _, _ ->},
    onNavigateUp: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPopupVisible by remember { mutableStateOf(false) }
    var iconPosition by remember { mutableStateOf(IntOffset.Zero) }

    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = modifier.height(45.dp),
            onClick = onNavigateUp
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = searchKeyWords,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = searchKeyWords,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 4.dp),
                )
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            modifier = modifier.height(45.dp),
            onClick = onClickSearch
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_search_24),
                contentDescription = "Search Content",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            modifier = modifier
                .height(45.dp)
                .onGloballyPositioned {
                    iconPosition = it.localToWindow(Offset.Zero).round()
                },
            onClick = {
                isPopupVisible = true
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_filter_list_24),
                contentDescription = "Filter Content",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        filterWindow(isPopupVisible, iconPosition) { isPopupVisible = false }
    }
}

@Preview
@Composable
private fun SearchTopBarPreview() {
    AudioCrossTheme {
        SearchTopBar(
            onClickSearch = {}
        )
    }
}

@Composable
@Preview
fun SearchScreenPreview() {
    AudioCrossTheme {
        SearchScreenViewModeless(
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