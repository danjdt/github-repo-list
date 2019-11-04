package com.danjdt.domain

import com.danjdt.domain.mock.dummyRepositoriesFlow
import com.danjdt.domain.utils.assertRepositories
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
        val flow = dummyRepositoriesFlow()
        val list = collect(flow)!!

        assertNotNull(list)
        assertRepositories(list)
    }
}