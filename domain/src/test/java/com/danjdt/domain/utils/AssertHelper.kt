package com.danjdt.domain.utils

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
fun assertRepositories(list: List<Repository>) {
    for (item in list) {
        with(item) {
            assertEquals(10, id)
            assertEquals("Name", name)
            assertEquals("Description", description)
            assertEquals(500, forks)
            assertEquals(1000, stargazersCount)
            assertNotNull(owner)
        }
    }
}

fun assertPullRequests(list: List<PullRequest>) {
    for (item in list) {
        with(item) {
            TestCase.assertEquals(10, id)
            TestCase.assertEquals("Title", title)
            TestCase.assertEquals("Lorem ipsum dolor", body)
            TestCase.assertEquals("https://www.google.com/", htmlUrl)
            TestCase.assertNotNull(user)
        }
    }
}
