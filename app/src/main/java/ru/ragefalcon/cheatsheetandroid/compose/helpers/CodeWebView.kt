package ru.ragefalcon.cheatsheetandroid.compose.helpers

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
                        CodeWebView(
                            """
                               |fun main() {
                               |    println("Hello, World!")
                               |}
                            """.trimMargin(),
                            Modifier.weight(1f)
                        )
*/
@Composable
fun CodeWebView(code: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current as ComponentActivity

    Box(modifier){
        AndroidView(
            factory = { WebView(context) },
            update = { webView ->
                webView.settings.javaScriptEnabled = true
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                webView.settings.blockNetworkLoads = false
                webView.loadDataWithBaseURL(
                    null,
                    """<html>
                        <head>
                                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.24.1/themes/prism.min.css">
                                <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.24.1/prism.min.js"></script>
                                <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.24.1/components/prism-kotlin.min.js"></script>
                        <style>body{font-family:monospace;white-space: pre;}</style>
                        </head>
                        <body>
                                <pre><code class="language-kotlin">$code</code></pre>
                        </body>
                        </html>""",
                    "text/html",
                    "UTF-8",
                    null
                )
                webView.webViewClient = WebViewClient()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
