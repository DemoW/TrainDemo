package lishui.study.common.crash;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by lishui.lin on 19-8-19
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "demo_crash" + File.separator;
    private static final String FILE_NAME = "crash_log";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private Context mContext;
    private String mFinalPath;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private CrashHandler() {
    }

    private static class SingleHolder{
        private static final CrashHandler sInstance = new CrashHandler();
    }

    public static CrashHandler getInstance(){
        return SingleHolder.sInstance;
    }

    public void init(Context context) {
        mContext = context;
        // 获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        dumpExceptionToDevice(ex);
        uploadExceptionToServer();

        ex.printStackTrace();

        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToDevice(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted, skip dump exception");
                return;
            }
        }

        boolean hasPermission = verifyStoragePermissions(mContext);
        if (hasPermission){
            File dir = new File(EXTERNAL_PATH);
            if (!dir.exists()) {
                boolean isSuccess = dir.mkdirs();
                if (!isSuccess) {
                    Log.w(TAG, "sdcard mounted, but can not create dirs: " + EXTERNAL_PATH);
                    // if make dirs fail, try to save crash log in private dirs
                    hasPermission = false;
                }
            }
        } else {
            Log.w(TAG, "sdcard mounted, but lack storage permission to write.");
        }

        String crashTime;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentTime = LocalDateTime.now();
            crashTime = currentTime.toString();
        } else {
            long currentTime = System.currentTimeMillis();
            crashTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));
        }

        // try to gain files locally in user version
        if (hasPermission) {
            mFinalPath = EXTERNAL_PATH + FILE_NAME + crashTime + FILE_NAME_SUFFIX;
        } else {
            mFinalPath = mContext.getFilesDir().getPath() + File.separator + FILE_NAME + crashTime + FILE_NAME_SUFFIX;
        }
        writeCrashLogToFile(mFinalPath, crashTime, ex);
    }

    private void writeCrashLogToFile(String path, String crashTime, Throwable ex) {
        File file = new File(path);

        try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            pw.println("=====crash log begin=====");
            pw.println("Crash time: "+crashTime);

            //导出手机信息
            dumpPhoneInfo(pw);

            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
            pw.println("=====crash log end=====");
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    public boolean verifyStoragePermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                int permission = ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return permission == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //upload crash file to server if need
    private void uploadExceptionToServer() {
        if (!TextUtils.isEmpty(mFinalPath)){
            if (DEBUG) Log.d(TAG, "Now upload crash log to server: " + mFinalPath);
        }
    }
}
