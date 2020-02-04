package lishui.demo.blur;

import android.content.Context;

/**
 * Created by lishui.lin on 19-12-25
 */
public class BlurManager {

    private static volatile BlurManager blurManager;
    private BlurProcessor mProcessor;

    private static BlurManager get( Context context) {
        if (blurManager == null) {
            synchronized (BlurManager.class) {
                if (blurManager == null) {
                    blurManager = new BlurManager(context);
                }
            }
        }

        return blurManager;
    }

    private BlurManager(Context context) {
         mProcessor = new RSBlurProcessor(context.getApplicationContext());
//        mProcessor = new NativeBlurProcessor();
    }

    private BlurProcessor getRequestBlurProcessor() {
        return mProcessor;
    }

    public static BlurProcessor with(Context context) {
        return BlurManager.get(context).getRequestBlurProcessor();
    }
}