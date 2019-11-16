package lishui.study.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.core.widget.ContentLoadingProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.study.R;
import lishui.study.common.log.LogUtil;
import lishui.study.common.web.WebClientListenerAdapter;
import lishui.study.common.web.WebViewManager;

public class BrowserActivity extends Activity{

    private static final String TAG = "BrowserActivity";

    @BindView(R.id.browser_view)
    WebView mWebView;
    @BindView(R.id.browser_loading_bar)
    ContentLoadingProgressBar mLoadingBar;

    private Intent mIntent;
    private WebViewManager mWebManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        initWebManager();
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new ViewClient());

        WebSettings s = mWebView.getSettings();
        s.setUseWideViewPort(true);
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setSavePassword(false);
        s.setSaveFormData(false);
        s.setBlockNetworkLoads(true);

        // Javascript is purposely disabled, so that nothing can be
        // automatically run.
        s.setJavaScriptEnabled(false);
        s.setDefaultTextEncodingName("utf-8");


        mIntent = getIntent();
        requestPermissionAndLoad();
    }

    private void initWebManager() {
        mWebManager = new WebViewManager(mWebView);
        mWebManager.setWebChromeClientListener(new WebClientListenerAdapter(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtil.d("onProgressChanged newProgress : " + newProgress);
            }
        });
    }

    private void loadUrl() {
        if (mIntent.hasExtra(Intent.EXTRA_TITLE)) {
            setTitle(mIntent.getStringExtra(Intent.EXTRA_TITLE));
        }
        mWebView.loadUrl(String.valueOf(mIntent.getData()));
        mLoadingBar.show();
    }

    private void requestPermissionAndLoad() {
        Uri destination = mIntent.getData();
        if (destination != null) {
            // Is this a local file?
            if ("file".equals(destination.getScheme())
                    && PackageManager.PERMISSION_DENIED ==
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                loadUrl();
            }
        }
    }

    private class ViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            mLoadingBar.hide();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent;
            // Perform generic parsing of the URI to turn it into an Intent.
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException ex) {
                LogUtil.w(TAG, "Bad URI " + url + ": " + ex.getMessage());
                Toast.makeText(BrowserActivity.this,
                        R.string.cannot_open_link, Toast.LENGTH_SHORT).show();
                return true;
            }
            // Sanitize the Intent, ensuring web pages can not bypass browser
            // security (only access to BROWSABLE activities).
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            Intent selector = intent.getSelector();
            if (selector != null) {
                selector.addCategory(Intent.CATEGORY_BROWSABLE);
                selector.setComponent(null);
            }
            // Pass the package name as application ID so that the intent from the
            // same application can be opened in the same tab.
            intent.putExtra(Browser.EXTRA_APPLICATION_ID,
                    view.getContext().getPackageName());

            try {
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                LogUtil.w(TAG, "No application can handle " + url);
                Toast.makeText(BrowserActivity.this,
                        R.string.cannot_open_link, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            final Uri uri = request.getUrl();
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())
                    && uri.getPath().endsWith(".gz")) {
                LogUtil.d(TAG, "Trying to decompress " + uri + " on the fly");
                try {
                    final InputStream in = new GZIPInputStream(
                            getContentResolver().openInputStream(uri));
                    final WebResourceResponse resp = new WebResourceResponse(
                            getIntent().getType(), "utf-8", in);
                    resp.setStatusCodeAndReasonPhrase(200, "OK");
                    return resp;
                } catch (IOException e) {
                    LogUtil.w(TAG, "Failed to decompress; falling back", e);
                }
            }
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}
