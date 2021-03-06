/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lishui.study.common.util;

import android.animation.ValueAnimator;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.TransactionTooLargeException;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lishui.study.common.constant.PreferenceConstant;
import lishui.study.common.log.LogUtils;


/**
 * Various utilities shared amongst the Launcher's classes.
 */
public final class Utilities {

    private static final String TAG = "Launcher.Utilities";

    private static final Pattern sTrimPattern =
            Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    private static final int[] sLoc0 = new int[2];
    private static final int[] sLoc1 = new int[2];

    public static final boolean ATLEAST_Q = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    public static final boolean ATLEAST_P =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;

    public static final boolean ATLEAST_OREO_MR1 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;

    public static final boolean ATLEAST_OREO =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    public static final int SINGLE_FRAME_MS = 16;

    /**
     * Set on a motion event dispatched from the nav bar. See {@link MotionEvent#setEdgeFlags(int)}.
     */
    public static final int EDGE_NAV_BAR = 1 << 8;

    /**
     * Set on a motion event do disallow any gestures and only handle touch.
     * See {@link MotionEvent#setEdgeFlags(int)}.
     */
    public static final int FLAG_NO_GESTURES = 1 << 9;

    public static boolean shouldDisableGestures(MotionEvent ev) {
        return (ev.getEdgeFlags() & FLAG_NO_GESTURES) == FLAG_NO_GESTURES;
    }

    /**
     * Indicates if the device has a debug build. Should only be used to store additional info or
     * add extra logging and not for changing the app behavior.
     */
    public static final boolean IS_DEBUG_DEVICE =
            Build.TYPE.toLowerCase(Locale.ROOT).contains("debug") ||
            Build.TYPE.toLowerCase(Locale.ROOT).equals("eng");

    public static boolean isDevelopersOptionsEnabled(Context context) {
        return Settings.Global.getInt(context.getApplicationContext().getContentResolver(),
                        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }

    // These values are same as that in {@link AsyncTask}.
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_TIME = 1;
    /**
     * An {@link Executor} to be used with async task with no limit on the queue size.
     */
    public static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(50));

    // 并行和串行线程池
    public static final Executor THREAD_PARALLEL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;
    public static final Executor THREAD_SERIAL_EXECUTOR = AsyncTask.SERIAL_EXECUTOR;

    // 主线程执行器
    public static final Executor MAIN_THREAD_EXECUTOR = new Executor() {
                private final Handler handler = new Handler(Looper.getMainLooper());

                @Override
                public void execute(@NonNull Runnable command) {
                    handler.post(command);
                }
            };

    public static boolean isLoggable(String propertyName) {
        return Log.isLoggable(propertyName, Log.VERBOSE);
    }

    /**
     * Sets {@param out} to be same as {@param in} by rounding individual values
     */
    public static void roundArray(float[] in, int[] out) {
       for (int i = 0; i < in.length; i++) {
           out[i] = Math.round(in[i]);
       }
    }

    public static void offsetPoints(float[] points, float offsetX, float offsetY) {
        for (int i = 0; i < points.length; i += 2) {
            points[i] += offsetX;
            points[i + 1] += offsetY;
        }
    }

    /**
     * Utility method to determine whether the given point, in local coordinates,
     * is inside the view, where the area of the view is expanded by the slop factor.
     * This method is called while processing touch-move events to determine if the event
     * is still within the view.
     */
    public static boolean pointInView(View v, float localX, float localY, float slop) {
        return localX >= -slop && localY >= -slop && localX < (v.getWidth() + slop) &&
                localY < (v.getHeight() + slop);
    }

    public static int[] getCenterDeltaInScreenSpace(View v0, View v1) {
        v0.getLocationInWindow(sLoc0);
        v1.getLocationInWindow(sLoc1);

        sLoc0[0] += (v0.getMeasuredWidth() * v0.getScaleX()) / 2;
        sLoc0[1] += (v0.getMeasuredHeight() * v0.getScaleY()) / 2;
        sLoc1[0] += (v1.getMeasuredWidth() * v1.getScaleX()) / 2;
        sLoc1[1] += (v1.getMeasuredHeight() * v1.getScaleY()) / 2;
        return new int[] {sLoc1[0] - sLoc0[0], sLoc1[1] - sLoc0[1]};
    }

    public static void scaleRectFAboutCenter(RectF r, float scale) {
        if (scale != 1.0f) {
            float cx = r.centerX();
            float cy = r.centerY();
            r.offset(-cx, -cy);
            r.left = r.left * scale;
            r.top = r.top * scale ;
            r.right = r.right * scale;
            r.bottom = r.bottom * scale;
            r.offset(cx, cy);
        }
    }

    public static void scaleRectAboutCenter(Rect r, float scale) {
        if (scale != 1.0f) {
            int cx = r.centerX();
            int cy = r.centerY();
            r.offset(-cx, -cy);
            scaleRect(r, scale);
            r.offset(cx, cy);
        }
    }

    public static void scaleRect(Rect r, float scale) {
        if (scale != 1.0f) {
            r.left = (int) (r.left * scale + 0.5f);
            r.top = (int) (r.top * scale + 0.5f);
            r.right = (int) (r.right * scale + 0.5f);
            r.bottom = (int) (r.bottom * scale + 0.5f);
        }
    }

    public static void insetRect(Rect r, Rect insets) {
        r.left = Math.min(r.right, r.left + insets.left);
        r.top = Math.min(r.bottom, r.top + insets.top);
        r.right = Math.max(r.left, r.right - insets.right);
        r.bottom = Math.max(r.top, r.bottom - insets.bottom);
    }

    public static float shrinkRect(Rect r, float scaleX, float scaleY) {
        float scale = Math.min(Math.min(scaleX, scaleY), 1.0f);
        if (scale < 1.0f) {
            int deltaX = (int) (r.width() * (scaleX - scale) * 0.5f);
            r.left += deltaX;
            r.right -= deltaX;

            int deltaY = (int) (r.height() * (scaleY - scale) * 0.5f);
            r.top += deltaY;
            r.bottom -= deltaY;
        }
        return scale;
    }

    /**
     * Maps t from one range to another range.
     * @param t The value to map.
     * @param fromMin The lower bound of the range that t is being mapped from.
     * @param fromMax The upper bound of the range that t is being mapped from.
     * @param toMin The lower bound of the range that t is being mapped to.
     * @param toMax The upper bound of the range that t is being mapped to.
     * @return The mapped value of t.
     */
    public static float mapToRange(float t, float fromMin, float fromMax, float toMin, float toMax,
            Interpolator interpolator) {
        if (fromMin == fromMax || toMin == toMax) {
            Log.e(TAG, "mapToRange: range has 0 length");
            return toMin;
        }
        float progress = getProgress(t, fromMin, fromMax);
        return mapRange(interpolator.getInterpolation(progress), toMin, toMax);
    }

    public static float getProgress(float current, float min, float max) {
        return Math.abs(current - min) / Math.abs(max - min);
    }

    public static float mapRange(float value, float min, float max) {
        return min + (value * (max - min));
    }

    public static boolean isSystemApp(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ComponentName cn = intent.getComponent();
        String packageName = null;
        if (cn == null) {
            ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if ((info != null) && (info.activityInfo != null)) {
                packageName = info.activityInfo.packageName;
            }
        } else {
            packageName = cn.getPackageName();
        }
        if (packageName != null) {
            try {
                PackageInfo info = pm.getPackageInfo(packageName, 0);
                return (info != null) && (info.applicationInfo != null) &&
                        ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            } catch (NameNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Trims the string, removing all whitespace at the beginning and end of the string.
     * Non-breaking whitespaces are also removed.
     */
    public static String trim(CharSequence s) {
        if (s == null) {
            return null;
        }

        // Just strip any sequence of whitespace or java space characters from the beginning and end
        Matcher m = sTrimPattern.matcher(s);
        return m.replaceAll("$1");
    }

    /**
     * Calculates the height of a given string at a specific text size.
     */
    public static int calculateTextHeight(float textSizePx) {
        Paint p = new Paint();
        p.setTextSize(textSizePx);
        Paint.FontMetrics fm = p.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    /**
     * Convenience println with multiple args.
     */
    public static void println(String key, Object... args) {
        StringBuilder b = new StringBuilder();
        b.append(key);
        b.append(": ");
        boolean isFirstArgument = true;
        for (Object arg : args) {
            if (isFirstArgument) {
                isFirstArgument = false;
            } else {
                b.append(", ");
            }
            b.append(arg);
        }
        System.out.println(b.toString());
    }

    public static boolean isRtl(Resources res) {
        return res.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Returns true if the intent is a valid launch intent for a launcher activity of an app.
     * This is used to identify shortcuts which are different from the ones exposed by the
     * applications' manifest file.
     *
     * @param launchIntent The intent that will be launched when the shortcut is clicked.
     */
    public static boolean isLauncherAppTarget(Intent launchIntent) {
        if (launchIntent != null
                && Intent.ACTION_MAIN.equals(launchIntent.getAction())
                && launchIntent.getComponent() != null
                && launchIntent.getCategories() != null
                && launchIntent.getCategories().size() == 1
                && launchIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && TextUtils.isEmpty(launchIntent.getDataString())) {
            // An app target can either have no extra or have ItemInfo.EXTRA_PROFILE.
            Bundle extras = launchIntent.getExtras();
            return extras == null || extras.keySet().isEmpty();
        }
        return false;
    }

    public static float dpiToPx(int size, DisplayMetrics metrics){
        float densityRatio = (float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (size / densityRatio);
    }

    public static int pxToDp(float size, DisplayMetrics metrics) {
        return (int) Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                size, metrics));
    }

    public static boolean isBootCompleted() {
        return "1".equals(getSystemProperty("sys.boot_completed", "1"));
    }

    public static String getSystemProperty(String property, String defaultValue) {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method getter = clazz.getDeclaredMethod("get", String.class);
            String value = (String) getter.invoke(null, property);
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        } catch (Exception e) {
            Log.d(TAG, "Unable to read system properties");
        }
        return defaultValue;
    }

    /**
     * Ensures that a value is within given bounds. Specifically:
     * If value is less than lowerBound, return lowerBound; else if value is greater than upperBound,
     * return upperBound; else return value unchanged.
     */
    public static int boundToRange(int value, int lowerBound, int upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }

    /**
     * @see #boundToRange(int, int, int).
     */
    public static float boundToRange(float value, float lowerBound, float upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }

    /**
     * @see #boundToRange(int, int, int).
     */
    public static long boundToRange(long value, long lowerBound, long upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }

    /**
     * Wraps a message with a TTS span, so that a different message is spoken than
     * what is getting displayed.
     * @param msg original message
     * @param ttsMsg message to be spoken
     */
    public static CharSequence wrapForTts(CharSequence msg, String ttsMsg) {
        SpannableString spanned = new SpannableString(msg);
        spanned.setSpan(new TtsSpan.TextBuilder(ttsMsg).build(),
                0, spanned.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanned;
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(
                PreferenceConstant.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getDevicePrefs(Context context) {
        return context.getSharedPreferences(
                PreferenceConstant.DEVICE_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static boolean areAnimationsEnabled(Context context) {
        return ATLEAST_OREO
                ? ValueAnimator.areAnimatorsEnabled()
                : !context.getSystemService(PowerManager.class).isPowerSaveMode();
    }

    public static boolean isWallpaperAllowed(Context context) {
        return context.getSystemService(WallpaperManager.class).isSetWallpaperAllowed();
    }

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                LogUtils.e(TAG, "Error closing", e);

            }
        }
    }

    public static boolean isBinderSizeError(Exception e) {
        return e.getCause() instanceof TransactionTooLargeException
                || e.getCause() instanceof DeadObjectException;
    }

    public static <T> T getOverrideObject(Class<T> clazz, Context context, int resId) {
        String className = context.getString(resId);
        if (!TextUtils.isEmpty(className)) {
            try {
                Class<?> cls = Class.forName(className);
                return (T) cls.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | ClassCastException | NoSuchMethodException | InvocationTargetException e) {
                LogUtils.e(TAG, "Bad overriden class", e);
            }
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method to post a runnable on the handler, skipping the synchronization barriers.
     */
    public static void postAsyncCallback(Handler handler, Runnable callback) {
        Message msg = Message.obtain(handler, callback);
        msg.setAsynchronous(true);
        handler.sendMessage(msg);
    }

    /**
     * Parses a string encoded using {@link #getPointString(int, int)}
     */
    public static Point parsePoint(String point) {
        String[] split = point.split(",");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    /**
     * Encodes a point to string to that it can be persisted atomically.
     */
    public static String getPointString(int x, int y) {
        return String.format(Locale.ENGLISH, "%d,%d", x, y);
    }

    public static void unregisterReceiverSafely(Context context, BroadcastReceiver receiver) {
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {}
    }

    public static int[] getIntArrayFromString(String tokenized) {
        StringTokenizer tokenizer = new StringTokenizer(tokenized, ",");
        int[] array = new int[tokenizer.countTokens()];
        int count = 0;
        while (tokenizer.hasMoreTokens()) {
            array[count] = Integer.parseInt(tokenizer.nextToken());
            count++;
        }
        return array;
    }

    public static String getStringFromIntArray(int[] array) {
        StringBuilder str = new StringBuilder();
        for (int value : array) {
            str.append(value).append(",");
        }
        return str.toString();
    }

    public static float squaredHypot(float x, float y) {
        return x * x + y * y;
    }

    public static float squaredTouchSlop(Context context) {
        float slop = ViewConfiguration.get(context).getScaledTouchSlop();
        return slop * slop;
    }

    private static class FixedSizeEmptyDrawable extends ColorDrawable {

        private final int mSize;

        public FixedSizeEmptyDrawable(int size) {
            super(Color.TRANSPARENT);
            mSize = size;
        }

        @Override
        public int getIntrinsicHeight() {
            return mSize;
        }

        @Override
        public int getIntrinsicWidth() {
            return mSize;
        }
    }
}
