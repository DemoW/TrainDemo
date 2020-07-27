package lishui.demo.blur;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lishui.study.common.util.BitmapUtils;
import lishui.study.common.util.Utilities;

/**
 * Created by lishui.lin on 2020/2/4
 */
public class NativeBlurProcessor implements BlurProcessor{

    private static native void nativeBlur(
            Bitmap bitmapOut, int radius, int threadCount, int threadIndex, int round);

    static {
        System.loadLibrary("blur-native");
    }

    @Override
    public void blur(Bitmap original, float radius, boolean canReuse, ImageView view) {
        if (Objects.isNull(view)) {
            return;
        }

        Bitmap bitmap = blurSync(original, radius, canReuse);

        Utilities.MAIN_THREAD_EXECUTOR.execute(()->{
            view.setImageBitmap(bitmap);
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
                width = height = Utilities.pxToDp(56f, res.getDisplayMetrics());
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

        radius = realRadiusConvert(radius);
        if (radius == 0) {
            return null;
        }

        Bitmap blurredBitmap;
        if (canReuse) {
            blurredBitmap = original;
        } else {
            blurredBitmap = original.copy(original.getConfig(), true);
        }

        // Bitmap blurredBitmap = original.copy(Bitmap.Config.ARGB_8888, true);

        int cores = Utilities.CPU_COUNT;

        List<NativeTask> horizontal = new ArrayList<>(cores);
        List<NativeTask> vertical = new ArrayList<>(cores);
        for (int i = 0; i < cores; i++) {
            horizontal.add(new NativeTask(blurredBitmap, (int) radius, cores, i, 1));
            vertical.add(new NativeTask(blurredBitmap, (int) radius, cores, i, 2));
        }

        try {
            Utilities.THREAD_POOL_EXECUTOR.invokeAll(horizontal);
        } catch (InterruptedException e) {
            return blurredBitmap;
        }

        try {
            Utilities.THREAD_POOL_EXECUTOR.invokeAll(vertical);
        } catch (InterruptedException e) {
            return blurredBitmap;
        }
        return blurredBitmap;
    }

    // 2..254 range in native
    private int realRadiusConvert(float radius) {
        int realRadius = (int) (radius * 254);
        if (realRadius <= 2 || realRadius >= 254) {
            realRadius = 0;
        }
        return realRadius;
    }

    private static class NativeTask implements Callable<Void> {
        private final Bitmap _bitmapOut;
        private final int _radius;
        private final int _totalCores;
        private final int _coreIndex;
        private final int _round;

        public NativeTask(Bitmap bitmapOut, int radius, int totalCores, int coreIndex, int round) {
            _bitmapOut = bitmapOut;
            _radius = radius;
            _totalCores = totalCores;
            _coreIndex = coreIndex;
            _round = round;
        }

        @Override public Void call() throws Exception {
            nativeBlur(_bitmapOut, _radius, _totalCores, _coreIndex, _round);
            return null;
        }
    }
}
