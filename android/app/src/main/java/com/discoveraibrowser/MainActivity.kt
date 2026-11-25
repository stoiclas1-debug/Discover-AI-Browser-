package com.discoveraibrowser

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    browserShell()
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@androidx.compose.runtime.Composable
private fun browserShell() {
    var addressText by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Enter a web address or search phrase.") }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    val browserView = remember { mutableStateOf<WebView?>(null) }
    val focusManager = LocalFocusManager.current
    val onPageChanged by rememberUpdatedState<(String, Boolean, Boolean) -> Unit> { url, back, forward ->
        if (url.startsWith("http://") || url.startsWith("https://")) {
            currentPage = url
            addressText = url
            message =
                if (url.startsWith("http://")) {
                    "Warning: this page uses an unencrypted HTTP connection."
                } else {
                    ""
                }
        }
        canGoBack = back
        canGoForward = forward
    }

    fun navigate() {
        val resolved = AddressResolver.resolve(addressText)
        if (resolved == null) {
            message = "Only valid HTTP or HTTPS addresses and search phrases are supported."
            return
        }
        message = ""
        focusManager.clearFocus()
        browserView.value?.loadUrl(resolved)
    }

    BackHandler(enabled = canGoBack) {
        browserView.value?.goBack()
    }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(12.dp),
    ) {
        Text("Discover AI Browser", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Search Google or enter address") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = { navigate() }),
            )
            Button(onClick = { navigate() }) { Text("Go") }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            TextButton(enabled = canGoBack, onClick = { browserView.value?.goBack() }) { Text("Back") }
            TextButton(enabled = canGoForward, onClick = { browserView.value?.goForward() }) { Text("Forward") }
            TextButton(enabled = currentPage.isNotEmpty(), onClick = { browserView.value?.reload() }) { Text("Reload") }
            TextButton(onClick = {
                browserView.value?.apply {
                    stopLoading()
                    clearHistory()
                    loadUrl("about:blank")
                }
                currentPage = ""
                addressText = ""
                message = "Enter a web address or search phrase."
                canGoBack = false
                canGoForward = false
            }) { Text("Home") }
        }
        if (message.isNotEmpty()) {
            Text(message, modifier = Modifier.padding(bottom = 8.dp), style = MaterialTheme.typography.bodyMedium)
        }
        AndroidView(
            modifier = Modifier.fillMaxWidth().weight(1f),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = false
                        domStorageEnabled = false
                        allowFileAccess = false
                        allowContentAccess = false
                        javaScriptCanOpenWindowsAutomatically = false
                        setSupportMultipleWindows(false)
                        mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) safeBrowsingEnabled = true
                    }
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)
                    webViewClient =
                        object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest,
                            ): Boolean {
                                val scheme = request.url.scheme?.lowercase()
                                val isInternalBlankPage = request.url.toString() == "about:blank"
                                return scheme !in setOf("http", "https") && !isInternalBlankPage
                            }

                            override fun onPageStarted(
                                view: WebView,
                                url: String,
                                favicon: android.graphics.Bitmap?,
                            ) {
                                onPageChanged(url, view.canGoBack(), view.canGoForward())
                            }

                            override fun onPageFinished(
                                view: WebView,
                                url: String,
                            ) {
                                onPageChanged(url, view.canGoBack(), view.canGoForward())
                            }
                        }
                    browserView.value = this
                }
            },
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            browserView.value?.apply {
                stopLoading()
                webChromeClient = null
                webViewClient = WebViewClient()
                destroy()
            }
            browserView.value = null
        }
    }
}
