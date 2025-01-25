package com.grayson.audiocross.testbase.rule

import com.grayson.audiocross.testbase.helper.TestCoroutineHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule: TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        TestCoroutineHelper.onSetUp()
    }

    override fun finished(description: Description?) {
        super.finished(description)
        TestCoroutineHelper.onTearDown()
    }
}