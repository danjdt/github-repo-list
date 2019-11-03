package com.danjdt.githubjavarepos.extensions

import junit.framework.TestCase

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class IntDecimalFormatTest: TestCase() {

    fun testFormatBigIntToDecimal() {
        val bitInt = Integer.MAX_VALUE
        val formattedInt = bitInt.decimalFormat()
        assertEquals("2.147.483.647", formattedInt)
    }

    fun testFormatSmallIntToDecimal() {
        val smallInt = 3
        val formattedInt = smallInt.decimalFormat()
        assertEquals("3", formattedInt)
    }

    fun testFormatNegativeIntToDecimal() {
        val negInt = Integer.MIN_VALUE
        val formattedInt = negInt.decimalFormat()
        assertEquals("-2.147.483.648", formattedInt)
    }
}