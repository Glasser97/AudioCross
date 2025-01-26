package com.grayson.audiocross.testbase.helper

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
object TestCoroutineHelper {
    /**
     * 单元测试里面用的 Dispatcher
     */
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    /**
     * setUp 时调用
     */
    fun onSetUp() {
        mockDispatcherIO()
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    /**
     * tearDown() 时调用
     */
    fun onTearDown() {
        unmockDispatcher()
        Dispatchers.resetMain()
    }

    /**
     * 运行包含协程的代码
     */
    fun runTest(block: suspend TestScope.() -> Unit) =
        runTest(testCoroutineDispatcher) { block() }

    /**
     * 提供对Dispatcher.IO的mock
     */
    fun mockDispatcherIO() {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } answers {
            testCoroutineDispatcher
        }
        every { Dispatchers.Default } answers {
            testCoroutineDispatcher
        }
    }

    fun unmockDispatcher() {
        unmockkStatic(Dispatchers::class)
    }
}