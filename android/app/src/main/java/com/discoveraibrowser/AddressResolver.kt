package com.discoveraibrowser

import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal object AddressResolver {
    private val schemePrefix = Regex("^[A-Za-z][A-Za-z0-9+.-]*:")
    private val hostWithPort = Regex("^[A-Za-z0-9.-]+:\\d+(?:[/?#].*)?$")
    private val webScheme = Regex("^https?://", RegexOption.IGNORE_CASE)
    private val whitespace = Regex("\\s")
    private val searchOperators =
        setOf("allintext", "allintitle", "allinurl", "after", "before", "filetype", "intext", "intitle", "inurl", "site")
    private val blockedSchemes = setOf("about", "blob", "content", "data", "file", "ftp", "intent", "javascript", "mailto", "vbscript")

    fun resolve(input: String): String? {
        val value = input.trim()
        if (value.isEmpty()) return null

        val isWebUrl = webScheme.containsMatchIn(value)
        val scheme = schemePrefix.find(value)?.value?.dropLast(1)?.lowercase()
        val isHostWithPort = hostWithPort.matches(value)
        val isSearchOperator = scheme in searchOperators
        val isSearchPhrase = scheme != null && value.substringAfter(':').firstOrNull()?.isWhitespace() == true
        val hasUnsupportedScheme =
            scheme != null && !isWebUrl && !isHostWithPort &&
                (scheme in blockedSchemes || (!isSearchOperator && !isSearchPhrase))
        if (hasUnsupportedScheme || (value.contains("://") && !isWebUrl)) return null

        val candidate =
            when {
                isWebUrl -> value
                isHostWithPort -> "https://$value"
                isSearchOperator || isSearchPhrase -> searchUrl(value)
                !whitespace.containsMatchIn(value) && ('.' in value || value.equals("localhost", ignoreCase = true)) -> "https://$value"
                else -> "https://www.google.com/search?q=${URLEncoder.encode(value, StandardCharsets.UTF_8.name()).replace("+", "%20")}"
            }

        return runCatching { URI(candidate) }
            .getOrNull()
            ?.takeIf { uri ->
                uri.scheme?.lowercase() in setOf("http", "https") &&
                    !uri.host.isNullOrBlank() && uri.userInfo == null
            }
            ?.toASCIIString()
    }

    private fun searchUrl(query: String): String =
        "https://www.google.com/search?q=${URLEncoder.encode(query, StandardCharsets.UTF_8.name()).replace("+", "%20")}"
}
