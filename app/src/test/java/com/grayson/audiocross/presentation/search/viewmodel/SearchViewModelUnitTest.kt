package com.grayson.audiocross.presentation.search.viewmodel

import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.model.FetchAlbumListResult
import com.grayson.audiocross.domain.common.RequestResult
import com.grayson.audiocross.domain.search.usecase.SearchAlbumListUseCase
import com.grayson.audiocross.presentation.albumlist.model.AlbumListFilterParam
import com.grayson.audiocross.presentation.search.model.UiState
import com.grayson.audiocross.testbase.BaseUnitTest
import com.grayson.audiocross.testbase.helper.TestCoroutineHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelUnitTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule: TestRule = KoinTestRule.create {
        modules(
            module {
                single<SearchAlbumListUseCase> { searchAlbumListUseCase }
            }
        )
    }

    /**
     * test targets
     */
    private lateinit var viewModel: SearchViewModel
    private val searchAlbumListUseCase: SearchAlbumListUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = SearchViewModel()
        println("Set up complete. ViewModel initialized.")
    }

    @After
    fun tearDown() {
        println("Tear down complete.")
    }

    @Test
    fun `refreshAlbumList should updates albumList and listState on success`() =
        TestCoroutineHelper.runTest {
            // Arrangement
            val successResult = RequestResult.Success(
                FetchAlbumListResult(
                    albumItems = listOf(MockData.albumItem1, MockData.albumItem2),
                    currentPage = 1,
                    pageSize = 10,
                    totalCount = 2
                )
            )
            coEvery { searchAlbumListUseCase.fetch(any()) } returns successResult
            println("Triggering refreshAlbumList with success response.")


            // Act
            viewModel.refreshAlbumList()

            // Assert
            val albumList = viewModel.albumList.value
            val listState = viewModel.listState.value
            println("Verifying albumList and listState updates.")

            assertEquals(2, albumList.size)
            assertEquals(UiState.Success, listState.refreshState)
            assertEquals(UiState.Init, listState.loadMoreState)
        }

    @Test
    fun `refreshAlbumList should update listState to Error on failure`() =
        TestCoroutineHelper.runTest {
            // Arrangement
            val mockError = RequestResult.Error(Exception("Test Error"))
            coEvery { searchAlbumListUseCase.fetch(any()) } returns mockError

            // Act
            viewModel.refreshAlbumList()

            // Assert
            val albumList = viewModel.albumList.value
            val listState = viewModel.listState.value
            assertEquals(0, albumList.size)
            assertEquals(UiState.Error, listState.refreshState)
            assertEquals(UiState.Init, listState.loadMoreState)
        }


    @Test
    fun `updateKeywords should update keywords and trigger refresh`() =
        TestCoroutineHelper.runTest {
            // Arrangement
            val newKeyword = "Test Keyword"
            val successResult = RequestResult.Success(
                FetchAlbumListResult(
                    albumItems = listOf(MockData.albumItem1, MockData.albumItem2),
                    currentPage = 1,
                    pageSize = 10,
                    totalCount = 2
                )
            )
            coEvery { searchAlbumListUseCase.fetch(any()) } returns successResult

            // Act
            viewModel.updateKeywords(newKeyword)
            advanceUntilIdle()

            // Assert
            val keywords = viewModel.keywords.value
            val albumList = viewModel.albumList.value
            val listState = viewModel.listState.value
            assertEquals(newKeyword, keywords)
            assertEquals(2, albumList.size)
            assertEquals(UiState.Success, listState.refreshState)
            assertEquals(UiState.Init, listState.loadMoreState)
        }

    @Test
    fun `updateFilterParam should updates filterParam and trigger refresh`() =
        TestCoroutineHelper.runTest {
            // Arrangement
            val newFilterParam = AlbumListFilterParam(
                orderBy = OrderBy.ID,
                sortMethod = SortMethod.ASCENDING,
                hasSubtitle = true
            )
            val successResult = RequestResult.Success(
                FetchAlbumListResult(
                    albumItems = listOf(MockData.albumItem1, MockData.albumItem2),
                    currentPage = 1,
                    pageSize = 10,
                    totalCount = 2
                )
            )
            coEvery { searchAlbumListUseCase.fetch(any()) } returns successResult

            // Act
            viewModel.updateFilterParam(newFilterParam)
            advanceUntilIdle()

            // Assert
            val filterParam = viewModel.filterParam.value
            val albumList = viewModel.albumList.value
            val listState = viewModel.listState.value
            assertEquals(newFilterParam, filterParam)
            assertEquals(2, albumList.size)
            assertEquals(UiState.Success, listState.refreshState)
            assertEquals(UiState.Init, listState.loadMoreState)
        }

    @Test
    fun `loadMoreAlbumList should appends new data on Success`() = TestCoroutineHelper.runTest {
        // Arrangement
        val initialResult = RequestResult.Success(
                FetchAlbumListResult(
                    albumItems = listOf(MockData.albumItem1),
                    currentPage = 1,
                    pageSize = 1,
                    totalCount = 1
                )
            )
        val additionalResult = RequestResult.Success(
                FetchAlbumListResult(
                    albumItems = listOf(MockData.albumItem2),
                    currentPage = 2,
                    pageSize = 1,
                    totalCount = 1
                )
            )
        coEvery { searchAlbumListUseCase.fetch(any()) } returnsMany listOf(
            initialResult,
            additionalResult
        )

        // Act
        viewModel.refreshAlbumList()
        viewModel.loadMoreAlbumList()

        // Assert
        val albumList = viewModel.albumList.value
        val listState = viewModel.listState.value
        assertEquals(2, albumList.size)
        assertEquals(UiState.Success, listState.refreshState)
        assertEquals(UiState.Success, listState.loadMoreState)
    }

    @Test
    fun `loadMoreAlbumList should update listState to Error on Failure`() = TestCoroutineHelper.runTest {
        // Arrangement
        val initialResult = RequestResult.Success(
            FetchAlbumListResult(
                albumItems = listOf(MockData.albumItem1),
                currentPage = 1,
                pageSize = 1,
                totalCount = 1
            )
        )
        val additionalResult = RequestResult.Error(Exception())
        coEvery { searchAlbumListUseCase.fetch(any()) } returnsMany listOf(
            initialResult,
            additionalResult
        )

        // Act
        viewModel.refreshAlbumList()
        viewModel.loadMoreAlbumList()

        // Assert
        val albumList = viewModel.albumList.value
        val listState = viewModel.listState.value
        assertEquals(1, albumList.size)
        assertEquals(UiState.Success, listState.refreshState)
        assertEquals(UiState.Error, listState.loadMoreState)
    }


    private object MockData {

        val albumItem1: AlbumItem = AlbumItem(
            albumId = 1L,
            albumCode = "albumCode",
            title = "title",
            duration = 1000L,
            authorName = listOf("authorName1"),
            cover = AlbumCover(
                thumbnailCoverUrl = "thumbnailCoverUrl",
                mediumCoverUrl = "mediumCoverUrl",
                mainCoverUrl = "mainCoverUrl"
            )
        )

        val albumItem2: AlbumItem = AlbumItem(
            albumId = 2L,
            albumCode = "albumCode",
            title = "title",
            duration = 1000L,
            authorName = listOf("authorName2"),
            cover = AlbumCover(
                thumbnailCoverUrl = "thumbnailCoverUrl",
                mediumCoverUrl = "mediumCoverUrl",
                mainCoverUrl = "mainCoverUrl"
            )
        )
    }
}