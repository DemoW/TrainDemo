package lishui.demo.browser.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lishui.lin on 19-11-14
 */
public class WebViewUtils {

    public static final String BROWSER_TAG = "WebViewBrowser";

    private WebViewUtils() {
        throw new RuntimeException("WebViewUtils can not init");
    }

    public static final String DEFAULT_HOME_PAGE_URL = "https://www.baidu.com/";
    public static final Pattern BROWSER_URI_SCHEMA = Pattern.compile(
            "(?i)"   // switch on case insensitive matching
                    + "("    // begin group for schema
                    + "(?:http|https|file):\\/\\/"
                    + "|(?:inline|data|about|chrome|javascript):"
                    + ")"
                    + "(.*)");

    private static final Pattern WEBVIEW_VERSION_PATTERN =
            Pattern.compile("(Chrome/)([\\d.]+)\\s");

    // WebSettings.getUserAgentString()
    public static String getWebViewVersion(String userAgentString) {
        String mWebViewVersion;
        Matcher matcher = WEBVIEW_VERSION_PATTERN.matcher(userAgentString);
        if (matcher.find()) {
            mWebViewVersion = matcher.group(2);
        } else {
            mWebViewVersion = "-";
        }
        return mWebViewVersion;
    }

    public static String getUrlFromIntent(Intent intent) {
        return intent != null ? intent.getDataString() : null;
    }

    public static boolean startBrowsingIntent(Context context, String url) {
        Intent intent;
        // Perform generic parsing of the URI to turn it into an Intent.
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (Exception ex) {
            Log.w(BROWSER_TAG, "Bad URI " + url, ex);
            return false;
        }
        // Check for regular URIs that WebView supports by itself, but also
        // check if there is a specialized app that had registered itself
        // for this kind of an intent.
        Matcher m = BROWSER_URI_SCHEMA.matcher(url);
        if (m.matches() && !isSpecializedHandlerAvailable(context, intent)) {
            return false;
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
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException ex) {
            Log.w(BROWSER_TAG, "No application can handle " + url);
        } catch (SecurityException ex) {
            // This can happen if the Activity is exported="true", guarded by a permission, and sets
            // up an intent filter matching this intent. This is a valid configuration for an
            // Activity, so instead of crashing, we catch the exception and do nothing. See
            // https://crbug.com/808494 and https://crbug.com/889300.
            Log.w(BROWSER_TAG, "SecurityException when starting intent for " + url);
        }
        return false;
    }

    /**
     * Search for intent handlers that are specific to the scheme of the URL in the intent.
     */
    private static boolean isSpecializedHandlerAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> handlers = pm.queryIntentActivities(intent,
                PackageManager.GET_RESOLVED_FILTER);
        if (handlers == null || handlers.size() == 0) {
            return false;
        }
        for (ResolveInfo resolveInfo : handlers) {
            if (!isNullOrGenericHandler(resolveInfo.filter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNullOrGenericHandler(IntentFilter filter) {
        return filter == null
                || (filter.countDataAuthorities() == 0 && filter.countDataPaths() == 0);
    }

    /**
     * Hides the keyboard.
     * @param view The {@link View} that is currently accepting input.
     * @return Whether the keyboard was visible before.
     */
    public static boolean hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
