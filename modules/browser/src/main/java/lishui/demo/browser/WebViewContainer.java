package lishui.demo.browser;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lishui.lin on 19-11-14
 */
public class WebViewContainer extends FrameLayout {

    private Context mContext;

    public WebViewContainer(@NonNull Context context) {
        this(context, null);
    }

    public WebViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
}
