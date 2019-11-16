package lishui.study.common.util;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by lishui.lin on 19-11-15
 */
public class ImageUtils {

    private ImageUtils() {
        throw new RuntimeException("ImageUtils can not be created.");
    }

    public static int getExIfOrientation(String filepath) {
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(filepath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default: break;

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return degree;
    }
}
