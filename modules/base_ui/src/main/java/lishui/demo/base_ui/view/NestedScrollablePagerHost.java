package lishui.demo.base_ui.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import lishui.demo.base_ui.R;

/**
 * Created by lishui.lin on 19-11-16
 * 处理ViewPager2的嵌套滑动
 */
public class NestedScrollablePagerHost extends NestedScrollView {

    private final PointF mDownPos = new PointF();
    private final PointF mLastPos = new PointF();

    private ViewPager2 viewPager2;

    public NestedScrollablePagerHost(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollablePagerHost(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollablePagerHost(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewPager2 = findViewById(R.id.nested_view_pager2);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    private void handleInterceptTouchEvent(MotionEvent ev) {
        // ...
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPos.set(ev.getX(), ev.getY());
                viewPager2.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                final float xDistance = Math.abs(ev.getX() - mDownPos.x);
                final float yDistance = Math.abs(ev.getY() - mDownPos.y);
                if (xDistance > yDistance) {
                    viewPager2.requestDisallowInterceptTouchEvent(true);
                } else {
                    viewPager2.requestDisallowInterceptTouchEvent(false);
                }
            default:
                break;
        }

    }

}
