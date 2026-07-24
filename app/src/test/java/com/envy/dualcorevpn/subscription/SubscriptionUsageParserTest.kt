package com.envy.dualcorevpn.subscription

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SubscriptionUsageParserTest {
    @Test
    fun `parses standard userinfo header regardless of order and whitespace`() {
        val usage = SubscriptionUsageParser.parse(" download=20 ; upload=10; total=1000; expire=2000000000 ")!!
        assertEquals(10L, usage.uploadBytes)
        assertEquals(20L, usage.downloadBytes)
        assertEquals(30L, usage.usedBytes)
        assertEquals(1000L, usage.totalBytes)
        assertEquals(2_000_000_000L, usage.expiresAtEpochSeconds)
    }

    @Test
    fun `keeps valid values while ignoring malformed unknown and negative fields`() {
        assertEquals(
            SubscriptionUsage(downloadBytes = 20L),
            SubscriptionUsageParser.parse("upload=-1; download=20; total=overflow; anything=12"),
        )
    }

    @Test
    fun `used bytes does not overflow`() {
        assertNull(SubscriptionUsage(uploadBytes = Long.MAX_VALUE, downloadBytes = 1).usedBytes)
    }

    @Test
    fun `empty malformed and zero expiry carry no metadata`() {
        assertNull(SubscriptionUsageParser.parse(null))
        assertNull(SubscriptionUsageParser.parse("garbage"))
        assertEquals(SubscriptionUsage(), SubscriptionUsageParser.parse("expire=0"))
    }
}
