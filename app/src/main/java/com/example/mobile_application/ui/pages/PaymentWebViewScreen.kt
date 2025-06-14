package com.example.mobile_application.ui.pages

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PaymentWebViewScreen(
    paymentUrl: String,
    onSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val client = remember {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()

                if (url.contains("/payment-success")) {
                    onSuccess()
                    return true
                }

                if (url.contains("/payment-cancel")) {
                    onCancel()
                    return true
                }

                return false
            }
        }
    }

    AndroidView(
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = client
                loadUrl(paymentUrl)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
