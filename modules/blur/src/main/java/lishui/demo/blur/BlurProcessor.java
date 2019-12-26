package lishui.demo.blur;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

/**
 * Created by lishui.lin on 19-12-25
 */
public interface BlurProcessor {


    void blur(Bitmap original, float radius, boolean canReuse, ImageView view);

    void blur(Resources res, @DrawableRes int id, float radius, ImageView view);

    /**
     * Process the given image, blurring by the supplied radius.
     * If radius is 0, this will return original
     * @param original the bitmap to be blurred
     * @param radius the radius in pixels to blur the image
     * @param canReuse resue the original bitmap
     * @return the blurred version of the image.
     */
    Bitmap blurAsync(Bitmap original, float radius, boolean canReuse);


    default boolean isReady() {
        return false;
    }
}
