package lishui.demo.browser;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import lishui.demo.browser.util.WebViewUtils;

import static lishui.demo.browser.util.WebViewUtils.BROWSER_TAG;

public class WebViewBrowser extends Activity implements WebViewHeader.WebHeaderCallback, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "WebViewBrowser";

    // Our imaginary Android permission to associate with the WebKit geo permission
    private static final String RESOURCE_GEO = "RESOURCE_GEO";
    // Our imaginary WebKit permission to request when loading a file:// URL
    private static final String RESOURCE_FILE_URL = "RESOURCE_FILE_URL";
    // WebKit permissions with no corresponding Android permission can always be granted
    private static final String NO_ANDROID_PERMISSION = "NO_ANDROID_PERMISSION";

    private static final int WEBVIEW_RESET = 0x1001;
    private static final int WEBVIEW_CLEAR_CACHE = 0x1002;
    private static final int WEBVIEW_PRINT = 0x1003;
    private static final int WEBVIEW_ABOUT = 0x1004;


    private EditText mUrlBar;
    private WebView mWebView;
    private View mFullscreenView;
    private ViewGroup mWebContainer;
    private WebViewHeader mWebViewHeader;

    // The Bundle key for WebView serialized state
    private static final String SAVE_RESTORE_STATE_KEY = "WEBVIEW_CHROMIUM_STATE";
    // Maximal size of this state.
    private static final int MAX_STATE_LENGTH = 300 * 1024;

    // Map from WebKit permissions to Android permissions
    private static final HashMap<String, String> sPermissions;
    static {
        sPermissions = new HashMap<String, String>();
        sPermissions.put(RESOURCE_GEO, Manifest.permission.ACCESS_FINE_LOCATION);
        sPermissions.put(RESOURCE_FILE_URL, Manifest.permission.READ_EXTERNAL_STORAGE);
        sPermissions.put(PermissionRequest.RESOURCE_AUDIO_CAPTURE,
                Manifest.permission.RECORD_AUDIO);
        sPermissions.put(PermissionRequest.RESOURCE_MIDI_SYSEX, NO_ANDROID_PERMISSION);
        sPermissions.put(PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID, NO_ANDROID_PERMISSION);
        sPermissions.put(PermissionRequest.RESOURCE_VIDEO_CAPTURE,
                Manifest.permission.CAMERA);
    }

    // Each time we make a request, store it here with an int key. onRequestPermissionsResult will
    // look up the request in order to grant the approprate permissions.
    private SparseArray<PermissionRequest> mPendingRequests = new SparseArray<PermissionRequest>();
    private int mNextRequestKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_browser);

        getWebViewHeader().initialize(this);
        mUrlBar = (EditText) getWebViewHeader().getUrlBar();
        mUrlBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    getWebViewHeader().loadUrlFromUrlBar(view);
                    return true;
                }
                return false;
            }
        });

        createAndInitializeWebView();

        String url = WebViewUtils.getUrlFromIntent(getIntent());
        if (url == null) {
            mWebView.restoreState(savedInstanceState);
            url = mWebView.getUrl();
            if (url != null) {
                // If we have restored state, and that state includes
                // a loaded URL, we reload. This allows us to keep the
                // scroll offset, and also doesn't add an additional
                // navigation history entry.

                getWebViewHeader().setUrlBarText(url);

                // The immediately previous loadUrlFromurlbar must
                // have got as far as calling loadUrl, so there is no
                // URI parsing error at this point.

                getWebViewHeader().setUrlFail(false);
                WebViewUtils.hideKeyboard(mUrlBar);
                mWebView.reload();
                mWebView.requestFocus();
                return;
            }
            // Make sure to load a blank page to make it immediately inspectable with
            // chrome://inspect.
            url = "about:blank";
        }
        getWebViewHeader().setUrlBarText(url);
        getWebViewHeader().setUrlFail(false);
        getWebViewHeader().loadUrlFromUrlBar(mUrlBar);
    }

    private void createAndInitializeWebView() {
        WebView webview = new WebView(this);
        WebSettings settings = webview.getSettings();
        initializeSettings(settings);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                getWebViewHeader().showHeaderProgressView(true);
                getWebViewHeader().setUrlBarText(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                getWebViewHeader().setUrlBarText(url);
                getWebViewHeader().showHeaderProgressView(false);
            }

            @SuppressWarnings("deprecation") // because we support api level 19 and up.
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                // "about:" and "chrome:" schemes are internal to Chromium;
                // don't want these to be dispatched to other apps.
                if (url.startsWith("about:") || url.startsWith("chrome:")) {
                    return false;
                }
                return WebViewUtils.startBrowsingIntent(WebViewBrowser.this, url);
            }

            @SuppressWarnings("deprecation") // because we support api level 19 and up.
            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                getWebViewHeader().setUrlFail(true);
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                getWebViewHeader().updateHeaderProgress(newProgress);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(
                        new int[] {Color.TRANSPARENT}, 1, 1, Bitmap.Config.ARGB_8888);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // Pre Lollipop versions (< api level 21) do not have PermissionRequest,
                    // hence grant here immediately.
                    callback.invoke(origin, true, false);
                    return;
                }

                onPermissionRequest(new GeoPermissionRequest(origin, callback));
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                WebViewBrowser.this.requestPermissionsForPage(request);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mFullscreenView != null) {
                    ((ViewGroup) mFullscreenView.getParent()).removeView(mFullscreenView);
                }
                mFullscreenView = view;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addContentView(mFullscreenView,
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
            }

            @Override
            public void onHideCustomView() {
                if (mFullscreenView == null) {
                    return;
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((ViewGroup) mFullscreenView.getParent()).removeView(mFullscreenView);
                mFullscreenView = null;
            }
        });

        mWebView = webview;
        getContainer().addView(
                webview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Set default home page
        getWebViewHeader().setUrlBarText(WebViewUtils.DEFAULT_HOME_PAGE_URL);
    }

    ViewGroup getContainer() {
        if (mWebContainer == null) {
            mWebContainer = findViewById(R.id.web_container);
        }
        return mWebContainer;
    }

    WebViewHeader getWebViewHeader() {
        if (mWebViewHeader == null) {
            mWebViewHeader = findViewById(R.id.web_header);
        }
        return mWebViewHeader;
    }

    // setGeolocationDatabasePath deprecated in api level 24,
    // but we still use it because we support api level 19 and up.
    private void initializeSettings(WebSettings settings) {
        File appcache = null;

        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        appcache = getDir("appcache", 0);
        StrictMode.setThreadPolicy(oldPolicy);

        settings.setJavaScriptEnabled(true);

        // configure local storage apis and their database paths.
        settings.setAppCachePath(appcache.getPath());

        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        // Default layout behavior for chrome on android.
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
    }

    @Override
    public void loadBrowserUrl(String url) {
        // Request read access if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && "file".equals(Uri.parse(url).getScheme())
                && PackageManager.PERMISSION_DENIED
                == checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissionsForPage(new FilePermissionRequest(url));
        }

        // If it is file:// and we don't have permission, they'll get the "Webpage not available"
        // "net::ERR_ACCESS_DENIED" page. When we get permission, FilePermissionRequest.grant()
        // will reload.
        if (mWebView != null) {
            mWebView.loadUrl(url);
            mWebView.requestFocus();
        }
    }

    @Override
    public void actionBrowserMenu(View view) {
        showPopupWindow(view);
    }

    @Override
    public void exitBrowser() {
        finish();
    }

    public void showPopupWindow(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.getMenu().add(Menu.NONE, WEBVIEW_RESET, 0, R.string.menu_reset_web_view);
        popup.getMenu().add(Menu.NONE, WEBVIEW_CLEAR_CACHE, 1, R.string.menu_clear_cache);
        popup.getMenu().add(Menu.NONE, WEBVIEW_PRINT, 2, R.string.menu_print);
        popup.getMenu().add(Menu.NONE, WEBVIEW_ABOUT, 3, R.string.menu_about);
//        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case WEBVIEW_RESET:
                if (mWebView != null) {
                    ViewGroup container = getContainer();
                    container.removeView(mWebView);
                    mWebView.destroy();
                    mWebView = null;
                }
                createAndInitializeWebView();
                return true;
            case WEBVIEW_CLEAR_CACHE:
                if (mWebView != null) {
                    mWebView.clearCache(true);
                }
                return true;
            case WEBVIEW_PRINT:
                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                String jobName = "WebViewBrowser document";
                PrintDocumentAdapter printAdapter = mWebView.createPrintDocumentAdapter(jobName);
                if (printManager != null) {
                    printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
                }
                return true;
            case WEBVIEW_ABOUT:
                aboutBrowser();
                WebViewUtils.hideKeyboard(mUrlBar);
                return true;
            default:
                return false;
        }
    }

    private void aboutBrowser() {
        WebSettings settings = mWebView.getSettings();
        StringBuilder summary = new StringBuilder();
        summary.append("WebView version : " + WebViewUtils.getWebViewVersion(
                mWebView.getSettings().getUserAgentString()) + "\n");

        for (Method method : settings.getClass().getMethods()) {
            if (!methodIsSimpleInspector(method)) continue;
            try {
                summary.append(method.getName()).append(" : ").append(method.invoke(settings)).append("\n");
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_about))
                .setMessage(summary)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    // Returns true is a method has no arguments and returns either a boolean or a String.
    private boolean methodIsSimpleInspector(Method method) {
        Class<?> returnType = method.getReturnType();
        return ((returnType.equals(boolean.class) || returnType.equals(String.class))
                && method.getParameterTypes().length == 0);
    }

    private void requestPermissionsForPage(PermissionRequest request) {
        // Deny any unrecognized permissions.
        for (String webkitPermission : request.getResources()) {
            if (!sPermissions.containsKey(webkitPermission)) {
                Log.w(BROWSER_TAG, "Unrecognized WebKit permission: " + webkitPermission);
                request.deny();
                return;
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            request.grant(request.getResources());
            return;
        }

        // Find what Android permissions we need before we can grant these WebKit permissions.
        ArrayList<String> androidPermissionsNeeded = new ArrayList<String>();
        for (String webkitPermission : request.getResources()) {
            if (!canGrant(webkitPermission)) {
                // We already checked for unrecognized permissions, and canGrant will skip over
                // NO_ANDROID_PERMISSION cases, so this is guaranteed to be a regular Android
                // permission.
                String androidPermission = sPermissions.get(webkitPermission);
                androidPermissionsNeeded.add(androidPermission);
            }
        }

        // If there are no such Android permissions, grant the WebKit permissions immediately.
        if (androidPermissionsNeeded.isEmpty()) {
            request.grant(request.getResources());
            return;
        }

        // Otherwise, file a new request
        if (mNextRequestKey == Integer.MAX_VALUE) {
            Log.e(BROWSER_TAG, "Too many permission requests");
            return;
        }
        int requestCode = mNextRequestKey;
        mNextRequestKey++;
        mPendingRequests.append(requestCode, request);
        requestPermissions(androidPermissionsNeeded.toArray(new String[0]), requestCode);
    }

    private class FilePermissionRequest extends PermissionRequest {
        private String mOrigin;

        public FilePermissionRequest(String origin) {
            mOrigin = origin;
        }

        @Override
        public Uri getOrigin() {
            return Uri.parse(mOrigin);
        }

        @Override
        public String[] getResources() {
            return new String[] { WebViewBrowser.RESOURCE_FILE_URL };
        }

        @Override
        public void grant(String[] resources) {
            if (resources.length == 1 && WebViewBrowser.RESOURCE_FILE_URL.equals(resources[0])) {
                // Try again now that we have read access.
                WebViewBrowser.this.mWebView.loadUrl(mOrigin);
            }
        }

        @Override
        public void deny() {
            // womp womp
        }
    }

    private static class GeoPermissionRequest extends PermissionRequest {
        private String mOrigin;
        private GeolocationPermissions.Callback mCallback;

        public GeoPermissionRequest(String origin, GeolocationPermissions.Callback callback) {
            mOrigin = origin;
            mCallback = callback;
        }

        @Override
        public Uri getOrigin() {
            return Uri.parse(mOrigin);
        }

        @Override
        public String[] getResources() {
            return new String[] { WebViewBrowser.RESOURCE_GEO };
        }

        @Override
        public void grant(String[] resources) {
            if (resources.length == 1 && WebViewBrowser.RESOURCE_GEO.equals(resources[0])) {
                mCallback.invoke(mOrigin, true, false);
            }
        }

        @Override
        public void deny() {
            mCallback.invoke(mOrigin, false, false);
        }
    }

    // WebKit permissions which can be granted because either they have no associated Android
    // permission or the associated Android permission has been granted
    @TargetApi(Build.VERSION_CODES.M)
    private boolean canGrant(String webkitPermission) {
        String androidPermission = sPermissions.get(webkitPermission);
        if (NO_ANDROID_PERMISSION.equals(androidPermission)) {
            return true;
        }
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(androidPermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Verify that we can now grant all the requested permissions. Note that although grant()
        // takes a list of permissions, grant() is actually all-or-nothing. If there are any
        // requested permissions not included in the granted permissions, all will be denied.
        PermissionRequest request = mPendingRequests.get(requestCode);
        mPendingRequests.delete(requestCode);
        for (String webkitPermission : request.getResources()) {
            if (!canGrant(webkitPermission)) {
                request.deny();
                return;
            }
        }
        request.grant(request.getResources());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Deliberately don't catch TransactionTooLargeException here.
        mWebView.saveState(savedInstanceState);

        // Drop the saved state of it is too long since Android N and above
        // can't handle large states without a crash.
        byte[] webViewState = savedInstanceState.getByteArray(SAVE_RESTORE_STATE_KEY);
        if (webViewState != null && webViewState.length > MAX_STATE_LENGTH) {
            savedInstanceState.remove(SAVE_RESTORE_STATE_KEY);
            String message = String.format(
                    Locale.US, "Can't save state: %dkb is too long", webViewState.length / 1024);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}
