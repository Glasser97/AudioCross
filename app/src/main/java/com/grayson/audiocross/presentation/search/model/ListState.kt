package com.grayson.audiocross.presentation.search.model

sealed class UiState {
    data object Init : UiState()
    data object Loading : UiState()
    data object Success : UiState()
    data object Error : UiState()
}

data class ListState(
    val refreshState: UiState,
    val loadMoreState: UiState
)