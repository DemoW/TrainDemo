package lishui.demo.blur.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.FrameLayout;

import java.util.Objects;

import lishui.demo.blur.util.BlurUtils;

/**
 * Created by lishui.lin on 19-12-26
 */
public class BlurLayout extends FrameLayout {

    private Context mContext;
    private Activity mActivity;

    public BlurLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof ContextThemeWrapper) {
            mActivity = (Activity) context;
        }
        mContext = context.getApplicationContext();

    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void blurTask(int progress) {
        new RenderTask().execute(progress);
    }

    class RenderTask extends AsyncTask<Integer, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.getWindow().getDecorView().setForeground(null);
        }


        @Override
        protected Bitmap doInBackground(Integer... integers) {
            int blurProgress = integers[0] ;

            if (Objects.isNull(mActivity)) {
                return null;
            } else {
                if (blurProgress == 0) {
                    return null;
                }

                return BlurUtils.getBitmapFromDecorView(mContext, mActivity.getWindow());
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mActivity == null) {
                return;
            }

            if(bitmap == null) {
                setBackground(null);
                setVisibility(GONE);
            } else {
                setBackground(new BitmapDrawable(getResources(),bitmap));
            }
        }
    }

}
