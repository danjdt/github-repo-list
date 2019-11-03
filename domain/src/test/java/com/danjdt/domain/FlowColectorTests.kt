package com.danjdt.domain

import com.danjdt.domain.dummy.dummyPullRequestFlow
import com.danjdt.domain.utils.collect
import junit.framework.TestCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class FlowColectorTests : TestCase() {

    @FlowPreview
    fun testValidateFlowCollectorReturnsData() = runBlocking {
        val flow = dummyPullRequestFlow()
        val data = collect(flow)!!

        for (pullRequest in data) {
            with(pullRequest) {
                assertEquals(10, id)
                assertEquals("Title", title)
                assertEquals("Lorem ipsum dolor", body)
                assertEquals("https://www.google.com/", htmlUrl)
                assertNotNull(user)
            }
        }
    }
}