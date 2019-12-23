package lishui.demo.browser;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;

import lishui.demo.browser.util.WebViewUtils;

/**
 * Created by lishui.lin on 19-11-14
 */
public class WebViewHeader extends RelativeLayout implements TextView.OnEditorActionListener {

    private static final int MAX_PROGRESS_LEVEL = 10000;

    private Context mContext;

    private EditText mUrlBar;
    private ImageView mExitImageView;
    private ImageView mActionImageView;
    private View mHeaderProgress;

    private WeakReference<WebHeaderCallback> mCallback;

    public void initialize(WebHeaderCallback callbacks) {
        mCallback = new WeakReference<>(callbacks);
    }

    public WebViewHeader(Context context) {
        this(context, null);
    }

    public WebViewHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mUrlBar = findViewById(R.id.url_text_bar);
        mExitImageView = findViewById(R.id.exit_image_view);
        mActionImageView = findViewById(R.id.action_image_view);
        mHeaderProgress = findViewById(R.id.header_progress);
        mHeaderProgress.getBackground().setLevel(MAX_PROGRESS_LEVEL);

        mUrlBar.setOnEditorActionListener(this);
        mExitImageView.setOnClickListener(v -> {
            if (mCallback != null && mCallback.get() != null) {
                mCallback.get().exitBrowser();
            }
        });
        mActionImageView.setOnClickListener(v -> {
            if (mCallback != null && mCallback.get() != null) {
                mCallback.get().actionBrowserMenu(mActionImageView);
            }
        });
    }

    public void updateHeaderProgress(int progress) {
        mHeaderProgress.getBackground().setLevel(MAX_PROGRESS_LEVEL - progress / 100 * MAX_PROGRESS_LEVEL);
    }

    public void showHeaderProgressView(boolean isShow) {
        mHeaderProgress.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public View getUrlBar() {
        return mUrlBar;
    }

    public void loadUrlFromUrlBar(View view) {
        String url = mUrlBar.getText().toString();
        // if (Uri.parse(url).getScheme() == null) url = "http://" + url;

        Matcher m = WebViewUtils.BROWSER_URI_SCHEMA.matcher(url);
        if (!m.matches()) {
            url = "https://m.baidu.com/s?word="+url;
        }

        setUrlBarText(url);
        setUrlFail(false);
        if (mCallback != null && mCallback.get() != null) {
            mCallback.get().loadBrowserUrl(url);
        }
        WebViewUtils.hideKeyboard(mUrlBar);
    }

    public void setUrlBarText(String url) {
        mUrlBar.setText(url, TextView.BufferType.EDITABLE);
    }

    public void setUrlFail(boolean fail) {
        mUrlBar.setTextColor(fail ? Color.RED : Color.BLACK);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Skip if it's not the right action
        if (actionId != EditorInfo.IME_ACTION_SEARCH) {
            return false;
        }
        loadUrlFromUrlBar(v);
        return true;
    }

    interface WebHeaderCallback {
        void loadBrowserUrl(String url);
        void actionBrowserMenu(View view);
        void exitBrowser();
    }
}
