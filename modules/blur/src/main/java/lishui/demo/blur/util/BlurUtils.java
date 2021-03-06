package lishui.demo.blur.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import lishui.demo.blur.BlurController;

/**
 * Created by lishui.lin on 19-12-25
 */
public class BlurUtils {

    public static Bitmap getBitmapFromDecorView(Context context, Window window) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);

        int w = point.x;
        int h = point.y;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        Canvas canvas = new Canvas(bitmap);
        window.getDecorView().draw(canvas);

        bitmap = BlurController.get(context).blurSync(bitmap, 0.5f, false);

        return bitmap;
    }
}
