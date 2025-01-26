package com.grayson.audiocross.presentation.albumlist.viewmodel

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.ErrorRecovery
import com.grayson.audiocross.domain.albumlist.model.AlbumCover
import com.grayson.audiocross.domain.albumlist.model.AlbumItem
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase

import com.grayson.audiocross.testbase.BaseUnitTest
import com.grayson.audiocross.testbase.helper.TestCoroutineHelper
import io.mockk.mockk
import androidx.paging.testing.asSnapshot
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase.Param
import io.mockk.coEvery

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.dsl.module
import org.koin.test.KoinTestRule

class AlbumListViewModelTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule: TestRule = KoinTestRule.create {
        modules(
            module {
                single<FetchAlbumListUseCase> { fetchAlbumListUseCase }
            }
        )
    }

    /**
     * test targets
     */
    private lateinit var viewModel: AlbumListViewModel
    private val fetchAlbumListUseCase: FetchAlbumListUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AlbumListViewModel()
        println("Set up complete. ViewModel initialized.")
    }

    @After
    fun tearDown() {
        println("Tear down complete.")
    }

    @Test
    fun `pagingDataFlow emits mapped data when use case returns data`() =
        TestCoroutineHelper.runTest {
            // Arrange
            val mockAlbums = listOf(MockData.albumItem1, MockData.albumItem2)
            coEvery {
                fetchAlbumListUseCase.fetch(any())
            } returns object : PagingSource<Param, AlbumItem>() {
                override fun getRefreshKey(state: PagingState<Param, AlbumItem>): Param? = null

                override suspend fun load(params: LoadParams<Param>): LoadResult<Param, AlbumItem> {
                    println("pagingDataFlow emits mapped data when use case returns data")
                    return LoadResult.Page(
                        data = mockAlbums,
                        prevKey = null,
                        nextKey = null
                    )
                }
            }

            // Act
            val items = viewModel.pagingDataFlow.asSnapshot()

            // Assert
            assertEquals(2, items.size)
        }

    @Test
    fun `pagingDataFlow update refresh state error when use case returns error`() =
        TestCoroutineHelper.runTest {
            // Arrange
            val exceptError = Exception("test error")
            coEvery {
                fetchAlbumListUseCase.fetch(any())
            } returns object : PagingSource<Param, AlbumItem>() {
                override fun getRefreshKey(state: PagingState<Param, AlbumItem>): Param? = null

                override suspend fun load(params: LoadParams<Param>): LoadResult<Param, AlbumItem> {
                    println("pagingDataFlow emits error when use case returns error")
                    return LoadResult.Error(exceptError)
                }
            }

            // Act
            var loadStates: CombinedLoadStates? = null
            val items = viewModel.pagingDataFlow.asSnapshot(
                onError = { combinedLoadStates ->
                    loadStates = combinedLoadStates
                    ErrorRecovery.RETRY
                }
            )

            // Assert
            assertEquals(0, items.size)
            assertEquals(LoadState.Error(exceptError), loadStates?.refresh)
        }

    @Test
    fun `pagingDataFlow append mapped data when use case returns append data`() =
        TestCoroutineHelper.runTest {
            // Arrange
            val mockAlbums = listOf(MockData.albumItem1, MockData.albumItem2)
            val exceptParams = Param(
                orderBy = OrderBy.CREATED_DATE,
                sortMethod = SortMethod.DESCENDING,
                hasSubtitle = false,
                page = 1
            )
            coEvery {
                fetchAlbumListUseCase.fetch(any())
            } returns object : PagingSource<Param, AlbumItem>() {
                override fun getRefreshKey(state: PagingState<Param, AlbumItem>): Param? = null

                override suspend fun load(params: LoadParams<Param>): LoadResult<Param, AlbumItem> {
                    println("pagingDataFlow emits mapped data when use case returns data")
                    val requestParam = params.key ?: exceptParams
                    return LoadResult.Page(
                        data = mockAlbums,
                        prevKey = null,
                        nextKey = if (requestParam.page >= 2) null else (exceptParams.copy(page = requestParam.page + 1))
                    )
                }
            }

            // Act
            val items = viewModel.pagingDataFlow.asSnapshot {
                scrollTo(2)
            }

            // Assert
            assertEquals(4, items.size)
        }

    @Test
    fun `pagingDataFlow update append state error when use case returns error`() =
        TestCoroutineHelper.runTest {
            // Arrange
            val mockAlbums = listOf(MockData.albumItem1, MockData.albumItem2)
            val exceptError = Exception("test error")
            val exceptParams = Param(
                orderBy = OrderBy.CREATED_DATE,
                sortMethod = SortMethod.DESCENDING,
                hasSubtitle = false,
                page = 1
            )
            coEvery {
                fetchAlbumListUseCase.fetch(any())
            } returns object : PagingSource<Param, AlbumItem>() {
                override fun getRefreshKey(state: PagingState<Param, AlbumItem>): Param? = null

                override suspend fun load(params: LoadParams<Param>): LoadResult<Param, AlbumItem> {
                    println("pagingDataFlow emits mapped data when use case returns data")
                    val requestParam = params.key ?: exceptParams
                    return if (requestParam.page == 1) {
                        LoadResult.Page(
                            data = mockAlbums,
                            prevKey = null,
                            nextKey = exceptParams.copy(page = requestParam.page + 1)
                        )
                    } else {
                        LoadResult.Error(exceptError)
                    }
                }
            }

            // Act
            var combinedLoadStates: CombinedLoadStates? = null
            val items = viewModel.pagingDataFlow.asSnapshot(
                onError = {
                    combinedLoadStates = it
                    ErrorRecovery.RETRY
                }
            ) {
                scrollTo(2)
            }

            // Assert
            assertEquals(2, items.size)
            assertTrue(combinedLoadStates?.refresh is LoadState.NotLoading)
            assertEquals(LoadState.Error(exceptError), combinedLoadStates?.append)
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