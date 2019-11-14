package lishui.study.common.web;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by lishui.lin on 19-11-14
 */
public interface WebChromeClientListener {

    default void onProgressChanged(WebView view, int newProgress) {}
    default void onReceivedTitle(WebView view, String title) {}
    default void onReceivedIcon(WebView view, Bitmap icon) {}
    default void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {}
}
