package lishui.study.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import lishui.study.R;
import lishui.study.common.log.LogUtil;

/**
 * Created by lishui.lin on 19-11-12
 */
public class Utils {

    public static final int MIN_TAB_COUNT_THRESHOLD = 5;

    public static final String WEBVIEW_ACTION = "lishui.intent.action.WebView";

    private static final String BROWSER_ACTION = "lishui.intent.action.BROWSER";

    public static void startWebViewBrowser(Context context, String url) {
        if (context instanceof Activity) {
            Intent intent = new Intent();
            intent.setAction(BROWSER_ACTION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            LogUtil.w("Utils startWebViewBrowser fail in url: " + url);
        }
    }
}
