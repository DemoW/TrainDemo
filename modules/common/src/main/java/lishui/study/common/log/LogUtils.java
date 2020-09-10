package lishui.study.common.log;

import android.util.Log;

import lishui.study.common.BuildConfig;

/**
 * Created by lishui.lin on 19-11-9
 *
 * Control the level for info and debug log as follow:
 * $ adb shell setprop log.tag.TrainDemo D   // the lowest switch to debug level
 * $ adb shell setprop log.tag.TrainDemo I   // the lowest switch to info level
 */
public class LogUtils {

    private static final String LOG_TAG = "TrainDemo";
    private static final boolean sLogInfo = true;  // Log.isLoggable(DEFAULT_TAG, Log.INFO)
    private static final boolean sLogDebug = true;  // Log.isLoggable(DEFAULT_TAG, Log.DEBUG)
    private static final boolean sLogVerbose = BuildConfig.DEBUG;

    public static void v(String... info) {
        if (sLogVerbose) {
            if (info.length == 2) {
                Log.v(LOG_TAG, info[0] + " # " + info[1]);
            } else {
                Log.v(LOG_TAG, info[0]);
            }
        }
    }

    public static void d(String... info) {
        if (sLogDebug) {
            if (info.length == 2) {
                Log.d(LOG_TAG, info[0] + " # " + info[1]);
            } else {
                Log.d(LOG_TAG, info[0]);
            }
        }
    }

    public static void d(boolean isDebug, String... info) {
        if (sLogDebug || isDebug) {
            if (info.length == 2) {
                Log.d(LOG_TAG, info[0] + " # " + info[1]);
            } else {
                Log.d(LOG_TAG, info[0]);
            }
        }
    }


    public static void i(String... info) {
        if (sLogInfo) {
            if (info.length == 2) {
                Log.i(LOG_TAG, info[0] + " # " + info[1]);
            } else {
                Log.i(LOG_TAG, info[0]);
            }
        }
    }

    public static void w(String... info) {
        if (info.length == 2) {
            Log.w(LOG_TAG, info[0] + " # " + info[1]);
        } else {
            Log.w(LOG_TAG, info[0]);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        Log.w(LOG_TAG, tag + " # " + msg, throwable);
    }

    public static void w(String msg, Throwable throwable) {
        Log.w(LOG_TAG, msg, throwable);
    }


    public static void e(String... info) {
        if (info.length == 2) {
            Log.e(LOG_TAG, info[0] + " # " + info[1]);
        } else {
            Log.e(LOG_TAG, info[0]);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(LOG_TAG, tag + " # " + msg, throwable);
    }

    public static void e(String msg, Throwable throwable) {
        Log.e(LOG_TAG, msg, throwable);
    }

    public static boolean isLogPropertyEnabled(String propertyName, int level) {
        return Log.isLoggable(propertyName, level);
    }
}
