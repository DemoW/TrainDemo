package lishui.demo.blur;

import android.content.Context;

/**
 * Created by lishui.lin on 19-12-25
 */
public class BlurController {

    private static volatile BlurController sBlurController;
    private BlurProcessor mProcessor;

    public static BlurProcessor get(Context context) {
        if (sBlurController == null) {
            synchronized (BlurController.class) {
                if (sBlurController == null) {
                    sBlurController = new BlurController(context);
                }
            }
        }

        return sBlurController.getRequestBlurProcessor();
    }

    private BlurController(Context context) {
        //mProcessor = new RSBlurProcessor(context.getApplicationContext());
        mProcessor = new NativeBlurProcessor();
    }

    private BlurProcessor getRequestBlurProcessor() {
        return mProcessor;
    }
}