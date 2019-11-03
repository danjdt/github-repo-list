package com.danjdt.githubjavarepos.extensions

import junit.framework.TestCase

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class ListAddTests : TestCase() {

    fun testConcatTwoLists() {
        val listA = listOf(0, 1, 2)
        val listB = listOf(3, 4, 5)
        val listC = listA.add(listB)

        assertEquals(listOf(0, 1, 2, 3, 4, 5), listC)
    }
}