package com.envy.dualcorevpn.subscription

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SubscriptionDeepLinkTest {
    @Test
    fun `parses explicit url and optional name without fetching`() {
        assertEquals(
            SubscriptionImportRequest("https://provider.example/sub?id=1", "Работа"),
            SubscriptionDeepLink.parse("lust://add?url=https%3A%2F%2Fprovider.example%2Fsub%3Fid%3D1&name=%D0%A0%D0%B0%D0%B1%D0%BE%D1%82%D0%B0"),
        )
    }

    @Test
    fun `supports encoded path form`() {
        assertEquals(
            SubscriptionImportRequest("https://provider.example/sub"),
            SubscriptionDeepLink.parse("lust://subscription/https%3A%2F%2Fprovider.example%2Fsub"),
        )
    }

    @Test
    fun `clipboard accepts only subscription urls and lust links`() {
        assertEquals(SubscriptionImportRequest("https://provider.example/sub"), SubscriptionClipboard.parse("  https://provider.example/sub  "))
        assertEquals(
            SubscriptionImportRequest("https://provider.example/sub"),
            SubscriptionClipboard.parse("lust://add?url=https%3A%2F%2Fprovider.example%2Fsub"),
        )
        assertNull(SubscriptionClipboard.parse("vless://uuid@example.test:443"))
        assertNull(SubscriptionClipboard.parse("file:///data/local"))
    }

    @Test
    fun `rejects unknown schemes hosts and non-http targets`() {
        assertNull(SubscriptionDeepLink.parse("https://provider.example/sub"))
        assertNull(SubscriptionDeepLink.parse("lust://connect?url=https%3A%2F%2Fprovider.example%2Fsub"))
        assertNull(SubscriptionDeepLink.parse("lust://add?url=file%3A%2F%2F%2Fdata%2Flocal"))
        assertNull(SubscriptionDeepLink.parse("lust://add?url=javascript%3Aalert(1)"))
    }
}
