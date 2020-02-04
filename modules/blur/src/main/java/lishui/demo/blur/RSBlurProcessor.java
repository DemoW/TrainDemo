package lishui.demo.blur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.ImageView;

import java.util.Objects;

import lishui.demo.blur.BlurProcessor;
import lishui.study.common.util.BitmapUtils;
import lishui.study.common.util.Utilities;

/**
 * Created by lishui.lin on 19-12-25
 */
public class RSBlurProcessor implements BlurProcessor {

    private static final String TAG = "RSBlurProcessor";

    private RenderScript mRenderScript;
    private boolean isInReadyState;

    public RSBlurProcessor(Context context) {
        this.mRenderScript = RenderScript.create(context);
        isInReadyState = true;
    }

    @Override
    public void blur(Bitmap original, float radius, boolean canReuseBitmap, ImageView view) {
        if (Objects.isNull(view)) {
            return;
        }

        Utilities.THREAD_POOL_EXECUTOR.execute(()->{
            Bitmap bitmap = blurSync(original, radius, canReuseBitmap);

            Utilities.MAIN_THREAD_EXECUTOR.execute(()->{
                view.setImageBitmap(bitmap);
            });
        });
    }

    @Override
    public void blur(Resources res, int id, float radius, ImageView view) {
        if (Objects.isNull(view)) {
            return;
        }

        Utilities.THREAD_POOL_EXECUTOR.execute(()->{
            int width = view.getWidth();
            int height = view.getHeight();
            if (width <= 0 || height <= 0) {
                width = height = Utilities.pxFromDp(56f, res.getDisplayMetrics());
            }
            Bitmap original = BitmapUtils.decodeResourceWithSampleSize(
                    res, id, width, height);

            Bitmap bitmap = blurSync(original, radius, true);

            Utilities.MAIN_THREAD_EXECUTOR.execute(()->{
                view.setImageBitmap(bitmap);
            });
        });
    }

    @Override
    public Bitmap blurSync(Bitmap original, float radius, boolean canReuse) {
        if (mRenderScript == null) {
            throw new IllegalStateException(
                    "RenderScript is destroyed, please recreate it.");
        }

        radius = realRadiusConvert(radius);
        if (radius == 0) {
            Log.w(TAG, "Radius out of range (0 < r <= 25), return original bitmap.");
            return original;
        }

        Bitmap blurredBitmap;
        Allocation input = null;
        Allocation output = null;
        ScriptIntrinsicBlur blurScript = null;

        if (canReuse) {
            blurredBitmap = original;
        } else {
            blurredBitmap = original.copy(original.getConfig(), true);
        }

        try {
            mRenderScript.setMessageHandler(new RenderScript.RSMessageHandler());
            input = Allocation.createFromBitmap(mRenderScript, blurredBitmap,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);

            output = Allocation.createTyped(mRenderScript, input.getType());
            blurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));

            blurScript.setInput(input);
            blurScript.setRadius(radius);
            blurScript.forEach(output);
            output.copyTo(blurredBitmap);

        } finally {
            if (input != null) {
                input.destroy();
            }
            if (output != null) {
                output.destroy();
            }
            if (blurScript != null) {
                blurScript.destroy();
            }
        }

        return blurredBitmap;
    }

    private void destroy() {
        if (mRenderScript != null) {
            mRenderScript.destroy();
            mRenderScript = null;
        }

        isInReadyState = false;
    }

    @Override
    public boolean isReady() {
        return isInReadyState;
    }

    // 0..25 range in rs
    private int realRadiusConvert(float radius) {
        int realRadius = (int) (radius * 25);
        if (realRadius < 0 || realRadius > 25) {
            realRadius = 0;
        }
        return realRadius;
    }

}
