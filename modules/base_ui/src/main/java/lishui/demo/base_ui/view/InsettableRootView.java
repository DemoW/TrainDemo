package lishui.demo.base_ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.view.WindowInsets;

import java.util.Collections;
import java.util.List;

import lishui.demo.base_ui.util.UiUtils;


public class InsettableRootView extends InsettableFrameLayout {

    private final Rect mTempRect = new Rect();

    @ViewDebug.ExportedProperty(category = "demo_ui")
    private final Rect mConsumedInsets = new Rect();

    @ViewDebug.ExportedProperty(category = "demo_ui")
    private static final List<Rect> SYSTEM_GESTURE_EXCLUSION_RECT =
            Collections.singletonList(new Rect());

    private WindowStateListener mWindowStateListener;
    @ViewDebug.ExportedProperty(category = "demo_ui")
    private boolean mDisallowBackGesture;

    public InsettableRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void handleSystemWindowInsets(Rect insets) {
        setInsets(insets);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        mTempRect.set(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        handleSystemWindowInsets(mTempRect);
        if (UiUtils.ATLEAST_Q) {
            return insets.inset(mConsumedInsets.left, mConsumedInsets.top,
                    mConsumedInsets.right, mConsumedInsets.bottom);
        } else {
            return insets.replaceSystemWindowInsets(mTempRect);
        }
    }

    @Override
    public void setInsets(Rect insets) {
        // If the insets haven't changed, this is a no-op. Avoid unnecessary layout caused by
        // modifying child layout params.
        if (!insets.equals(mInsets)) {
            super.setInsets(insets);
        }
    }

    public void dispatchInsets() {
        super.setInsets(mInsets);
    }

    public void setWindowStateListener(WindowStateListener listener) {
        mWindowStateListener = listener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mWindowStateListener != null) {
            mWindowStateListener.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (mWindowStateListener != null) {
            mWindowStateListener.onWindowVisibilityChanged(visibility);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        SYSTEM_GESTURE_EXCLUSION_RECT.get(0).set(l, t, r, b);
        setDisallowBackGesture(mDisallowBackGesture);
    }

    @TargetApi(Build.VERSION_CODES.Q)
    public void setDisallowBackGesture(boolean disallowBackGesture) {
        if (!UiUtils.ATLEAST_Q) {
            return;
        }
        mDisallowBackGesture = disallowBackGesture;
        setSystemGestureExclusionRects(mDisallowBackGesture
                ? SYSTEM_GESTURE_EXCLUSION_RECT
                : Collections.emptyList());
    }

    public interface WindowStateListener {

        void onWindowFocusChanged(boolean hasFocus);

        void onWindowVisibilityChanged(int visibility);
    }
}