package com.grayson.audiocross.testbase

import com.grayson.audiocross.testbase.rule.CoroutineTestRule
import com.grayson.audiocross.testbase.rule.LogMockRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.test.KoinTest


@Ignore("This is a base class for unit test.")
open class BaseUnitTest : KoinTest {

    @get:Rule
    val logMockRule: TestRule = LogMockRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule: TestRule = CoroutineTestRule()
}