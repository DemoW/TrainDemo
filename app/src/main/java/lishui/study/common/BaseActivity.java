package lishui.study.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import lishui.demo.base_ui.util.SystemUiController;
import lishui.study.config.FeatureFlags;
import lishui.study.view.GrayFrameLayout;


/**
 * Created by lishui.lin on 19-11-9
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected SystemUiController mSystemUiController;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        if (FeatureFlags.GRAY_SCREEN_ENABLED) {
            // 黑白屏全局实现方案二
            if ("FrameLayout".equals(name)) {
                int count = attrs.getAttributeCount();
                for (int i = 0; i < count; i++) {
                    String attrName = attrs.getAttributeName(i);
                    String attrValue = attrs.getAttributeValue(i);
                    if ("id".equals(attrName)) {
                        // eg: attrValue=@16908290, id=16908290, idValue=android:id/content
                        int id = Integer.parseInt(attrValue.substring(1));
                        String idValue = getResources().getResourceName(id);
                        if ("android:id/content".equals(idValue)) {
                            GrayFrameLayout grayFrameLayout = new GrayFrameLayout(context, attrs);
                            // grayFrameLayout.setBackground(getWindow().getDecorView().getBackground());
                            return grayFrameLayout;
                        }
                    }
                }
            }
        }
        return super.onCreateView(name, context, attrs);
    }

    public SystemUiController getSystemUiController() {
        if (mSystemUiController == null) {
            mSystemUiController = new SystemUiController(getWindow());
        }
        return mSystemUiController;
    }

    // 黑白屏全局实现方案一
    protected void greyDecorView() {
        if (FeatureFlags.GRAY_SCREEN_ENABLED && getLifecycle().getCurrentState() == Lifecycle.State.CREATED) {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

            final View decorView = getWindow().getDecorView();
            final Paint paint = new Paint();
            paint.setColorFilter(colorFilter);
            decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        }
    }

    public void startActivitySafely(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        }
    }
}
