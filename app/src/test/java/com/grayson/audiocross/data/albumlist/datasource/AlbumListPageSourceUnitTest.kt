package com.grayson.audiocross.data.albumlist.datasource

import androidx.paging.PagingSource
import com.grayson.audiocross.data.albumlist.clients.WorkClient
import com.grayson.audiocross.data.albumlist.model.work.PaginationWorks
import com.grayson.audiocross.data.albumlist.model.work.WorkInfo
import com.grayson.audiocross.domain.albumlist.base.OrderBy
import com.grayson.audiocross.domain.albumlist.base.SortMethod
import com.grayson.audiocross.domain.albumlist.usecase.FetchAlbumListUseCase
import com.grayson.audiocross.domain.common.RequestResult
import org.junit.Assert.*

import com.grayson.audiocross.testbase.BaseUnitTest
import com.grayson.audiocross.testbase.helper.TestCoroutineHelper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import org.junit.After
import org.junit.Before

import org.junit.Test

class AlbumListPageSourceUnitTest : BaseUnitTest() {

    @Before
    fun setUp() {
        mockkConstructor(WorkClient::class)
    }

    @After
    fun tearDown() {
        unmockkConstructor(WorkClient::class)
    }

    @Test
    fun `load returns successful page when data is available`(): Unit =
        TestCoroutineHelper.runTest {
            // Arrange
            val mockResponse = mockk<RequestResult.Success<PaginationWorks>>(relaxed = true) {
                coEvery { data.works } returns arrayOf(MockData.workInfo1, MockData.workInfo2)
                coEvery { data.pagination.currentPage } returns 1
            }
            coEvery { anyConstructed<WorkClient>().getWorks(any()) } returns mockResponse
            val initParam = FetchAlbumListUseCase.Param(
                orderBy = OrderBy.CREATED_DATE,
                sortMethod = SortMethod.ASCENDING,
                page = 1,
                hasSubtitle = false
            )
            val pagingSource = AlbumListPageSource(initParam)

            // Act
            val result = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = initParam,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )

            // Assert
            assert(result is PagingSource.LoadResult.Page)
            result as PagingSource.LoadResult.Page
            assertEquals(2, result.data.size)
            assertEquals(2, result.nextKey?.page)
            assertEquals(null, result.prevKey?.page)
        }

    @Test
    fun `load next key is null when data is empty`() = TestCoroutineHelper.runTest {
        TestCoroutineHelper.runTest {
            // Arrange
            val mockResponse = mockk<RequestResult.Success<PaginationWorks>>(relaxed = true) {
                coEvery { data.works } returns arrayOf()
                coEvery { data.pagination.currentPage } returns 1
            }
            coEvery { anyConstructed<WorkClient>().getWorks(any()) } returns mockResponse
            val initParam = FetchAlbumListUseCase.Param(
                orderBy = OrderBy.CREATED_DATE,
                sortMethod = SortMethod.ASCENDING,
                page = 1,
                hasSubtitle = false
            )
            val pagingSource = AlbumListPageSource(initParam)

            // Act
            val result = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = initParam,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )

            // Assert
            assert(result is PagingSource.LoadResult.Page)
            result as PagingSource.LoadResult.Page
            assertEquals(0, result.data.size)
            assertEquals(null, result.nextKey?.page)
            assertEquals(null, result.prevKey?.page)
        }
    }

    @Test
    fun `load returns error when data fetch fails`() = TestCoroutineHelper.runTest {
        val mockResponse = RequestResult.Error(Exception("test error"))
        coEvery { anyConstructed<WorkClient>().getWorks(any()) } returns mockResponse
        val initParam = FetchAlbumListUseCase.Param(
            orderBy = OrderBy.CREATED_DATE,
            sortMethod = SortMethod.ASCENDING,
            page = 1,
            hasSubtitle = false
        )
        val pagingSource = AlbumListPageSource(initParam)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = initParam,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Assert
        assert(result is PagingSource.LoadResult.Error)
        result as PagingSource.LoadResult.Error
        assertEquals("test error", result.throwable.message)
    }

    private object MockData {

        val workInfo1: WorkInfo = mockk<WorkInfo>(relaxed = true) {
            coEvery { id } returns 1L
        }
        val workInfo2: WorkInfo = mockk(relaxed = true) {
            coEvery { id } returns 2L
        }
    }
}