package lishui.study.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lishui.lin on 20-4-7
 */
public class GrayFrameLayout extends FrameLayout {

    private Paint mPaint = new Paint();
    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GrayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
