package com.discoveraibrowser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AddressResolverTest {
    @Test
    fun `adds https to a domain`() {
        assertEquals("https://example.com", AddressResolver.resolve("example.com"))
    }

    @Test
    fun `adds https to a host with a port`() {
        assertEquals("https://localhost:8080/path", AddressResolver.resolve("localhost:8080/path"))
    }

    @Test
    fun `supports host ports with query strings and fragments`() {
        assertEquals("https://localhost:8080?x=1", AddressResolver.resolve("localhost:8080?x=1"))
        assertEquals("https://example.com:8080#section", AddressResolver.resolve("example.com:8080#section"))
    }

    @Test
    fun `turns ordinary text into a search URL`() {
        assertEquals(
            "https://www.google.com/search?q=privacy%20first%20browser",
            AddressResolver.resolve("privacy first browser"),
        )
    }

    @Test
    fun `treats Google search operators as search phrases`() {
        assertEquals(
            "https://www.google.com/search?q=site%3Aexample.com%20cats",
            AddressResolver.resolve("site:example.com cats"),
        )
    }

    @Test
    fun `rejects non web schemes and user information`() {
        assertNull(AddressResolver.resolve("javascript:alert(1)"))
        assertNull(AddressResolver.resolve("file:///etc/passwd"))
        assertNull(AddressResolver.resolve("https://user:pass@example.com"))
    }

    @Test
    fun `returns null for blank input`() {
        assertNull(AddressResolver.resolve("  "))
    }
}
