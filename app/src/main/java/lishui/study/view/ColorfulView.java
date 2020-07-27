package lishui.study.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import lishui.study.R;
import lishui.study.common.util.Utilities;

public class ColorfulView extends RelativeLayout {

    private static final String TAG = "ColorfulView";
    private int mHeight;
    private int mWidth;
    private int dp80 = Utilities.pxToDp(80, getContext().getResources().getDisplayMetrics());
    private static final int DELAY_SHOW_TEXT = 0;
    private static boolean isFinishedAnim = false;
    private ImageView redIv, purpleIv, yellowIv, blueIv;

    private AnimHandler animHandler;

    public ColorfulView(Context context) {
        super(context);
    }

    public ColorfulView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfulView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(CENTER_VERTICAL, TRUE);
        lp.setMargins(0, 0, 0, dp80);

        purpleIv = new ImageView(getContext());
        purpleIv.setLayoutParams(lp);
        purpleIv.setImageResource(R.drawable.shape_circle_purple);
        addView(purpleIv);

        yellowIv = new ImageView(getContext());
        yellowIv.setLayoutParams(lp);
        yellowIv.setImageResource(R.drawable.shape_circle_yellow);
        addView(yellowIv);

        blueIv = new ImageView(getContext());
        blueIv.setLayoutParams(lp);
        blueIv.setImageResource(R.drawable.shape_circle_blue);
        addView(blueIv);

        redIv = new ImageView(getContext());
        redIv.setLayoutParams(lp);
        redIv.setImageResource(R.drawable.shape_circle_red);
        addView(redIv);

        setAnimation(redIv, redPath1);
        setAnimation(purpleIv, purplePath1);
        setAnimation(yellowIv, yellowPath1);
        setAnimation(blueIv, bluePath1);

    }

    private ViewPath redPath1, purplePath1, yellowPath1, bluePath1;

    private void initPath() {
        redPath1 = new ViewPath();
        redPath1.moveTo(0, 0);
        redPath1.lineTo(mWidth / 5 - mWidth / 2, 0);
        redPath1.curveTo(-700, -mHeight / 2, mWidth / 3 * 2, -mHeight / 3 * 2, 0, -dp80);

        purplePath1 = new ViewPath();
        purplePath1.moveTo(0, 0);
        purplePath1.lineTo(mWidth / 5 * 2 - mWidth / 2, 0);
        purplePath1.curveTo(-300, -mHeight / 2, mWidth, -mHeight / 9 * 5, 0, -dp80);

        yellowPath1 = new ViewPath();
        yellowPath1.moveTo(0, 0);
        yellowPath1.lineTo(mWidth / 5 * 3 - mWidth / 2, 0);
        yellowPath1.curveTo(300, mHeight, -mWidth, -mHeight / 9 * 5, 0, -dp80);

        bluePath1 = new ViewPath();
        bluePath1.moveTo(0, 0);
        bluePath1.lineTo(mWidth / 5 * 4 - mWidth / 2, 0);
        bluePath1.curveTo(700, mHeight / 3 * 2, -mWidth / 2, mHeight / 2, 0, -dp80);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isFinishedAnim){
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            initPath();
        }
    }

    public void startAnim() {
        // 判断是否已经加载过动画，避免重复加载
        if (!isFinishedAnim) {
//        removeAllViews();
            init();
            redAll.start();
            yellowAll.start();
            purpleAll.start();
            blueAll.start();

            // 内存泄露
     /*       new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoadContent();
                }
            }, 2400);*/

            // 修改方案
            animHandler = new AnimHandler(this);
            animHandler.sendEmptyMessageDelayed(DELAY_SHOW_TEXT, 2400);
        }else {
            showLoadContent();
        }
    }

    static class AnimHandler extends Handler{
        private WeakReference<ColorfulView> launcherViewWeakReference;
        public AnimHandler(ColorfulView launcherView){
            launcherViewWeakReference = new WeakReference<>(launcherView);
        }

        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (launcherViewWeakReference == null || launcherViewWeakReference.get() ==null)
                return;

            if (msg.what == DELAY_SHOW_TEXT) {
                launcherViewWeakReference.get().showLoadContent();
            }
        }
    }

    private void setAnimation(final ImageView target, ViewPath path) {
        ObjectAnimator anim = ObjectAnimator.ofObject(new ViewObj(target), "fabLoc",
                new ViewPathEvaluator(), path.getPoints().toArray());
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(2600);
        //组合添加缩放透明效果
        addAnimation(anim, target);
    }

    AnimatorSet redAll, purpleAll, yellowAll, blueAll;

    private void addAnimation(ObjectAnimator animator, final ImageView target) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 1000);
        valueAnimator.setDuration(1800);
        valueAnimator.setStartDelay(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float alpha = 1 - value / 2000;
                float scale = getScale(target) - 1;
                if (value <= 500) {
                    scale = 1 + (value / 500) * scale;
                } else {
                    scale = 1 + ((1000 - value) / 500) * scale;
                }
                target.setScaleX(scale);
                target.setScaleY(scale);
                target.setAlpha(alpha);
            }
        });
        valueAnimator.addListener(new AnimEndListener(target));
        if (target == redIv) {
            redAll = new AnimatorSet();
            redAll.playTogether(animator, valueAnimator);
        }
        if (target == blueIv) {
            blueAll = new AnimatorSet();
            blueAll.playTogether(animator, valueAnimator);
        }
        if (target == purpleIv) {
            purpleAll = new AnimatorSet();
            purpleAll.playTogether(animator, valueAnimator);
        }
        if (target == yellowIv) {
            yellowAll = new AnimatorSet();
            yellowAll.playTogether(animator, valueAnimator);
        }

    }


    private float getScale(ImageView target) {
        if (target == redIv)
            return 3.0f;
        if (target == purpleIv)
            return 2.0f;
        if (target == yellowIv)
            return 4.5f;
        if (target == blueIv)
            return 3.5f;
        return 2f;
    }


    private void showLoadContent() {

        TextView textView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);

        textView.setLayoutParams(layoutParams);
        textView.setText("a demo for train.");
        textView.setTextSize(24);
        addView(textView);
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView((target));
        }
    }


    public class ViewObj {
        private final ImageView img;

        public ViewObj(ImageView imageView) {
            this.img = imageView;
        }

        public void setFabLoc(ViewPoint newLoc) {
            img.setTranslationX(newLoc.x);
            img.setTranslationY(newLoc.y);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitAnim();
        isFinishedAnim = true;
    }

    private void exitAnim(){
        if (redAll != null && redAll.isRunning()){
            redAll.cancel();
            redAll.removeAllListeners();
            redAll = null;
        }

        if (yellowAll != null && yellowAll.isRunning()){
            yellowAll.cancel();
            yellowAll.removeAllListeners();
            yellowAll = null;
        }

        if (purpleAll != null && purpleAll.isRunning()){
            purpleAll.cancel();
            purpleAll.removeAllListeners();
            purpleAll = null;
        }

        if ((blueAll != null && blueAll.isRunning())){
            blueAll.cancel();
            blueAll.removeAllListeners();
            blueAll = null;
        }

        if (redPath1 != null){
            redPath1.clearPonits();
        }

        if (purplePath1 != null){
            purplePath1.clearPonits();
        }

        if (yellowPath1 != null){
            yellowPath1.clearPonits();
        }

        if (bluePath1 != null){
            bluePath1.clearPonits();
        }

        if (animHandler != null){
            animHandler.removeCallbacksAndMessages(null);
        }
    }
}


