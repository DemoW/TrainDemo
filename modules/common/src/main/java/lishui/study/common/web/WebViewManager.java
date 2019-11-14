package lishui.study.common.web;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;

/**
 * Created by lishui.lin on 19-11-14
 */
public class WebViewManager {

    private WebView mWebView;
    private WebChromeClientListener chromeClientListener;

    public WebViewManager(@NonNull WebView webView) {
        this.mWebView = webView;
    }

    public void setWebClientListener(WebClientListenerAdapter listener) {
        if (listener != null) {
            mWebView.setWebChromeClient(new ChromeClient());
            chromeClientListener = listener;
        } else {
            mWebView.setWebChromeClient(null);
            chromeClientListener = null;
        }
    }

    public void setWebChromeClientListener(WebChromeClientListener listener) {
        if (listener != null) {
            mWebView.setWebChromeClient(new ChromeClient());
            chromeClientListener = listener;
        } else {
            mWebView.setWebChromeClient(null);
            chromeClientListener = null;
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            chromeClientListener.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            chromeClientListener.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            chromeClientListener.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            chromeClientListener.onReceivedTouchIconUrl(view, url, precomposed);
        }
    }

    public void destroy() {
        mWebView.destroy();
    }
}
