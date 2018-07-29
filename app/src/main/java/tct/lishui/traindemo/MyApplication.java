package tct.lishui.traindemo;

import android.app.Application;
import android.os.Debug;
import android.os.Trace;
import android.util.Log;

/**
 * Created by lishui.lin on 18-7-27 16:52
 */
public class MyApplication extends Application {
	private static final String TAG = "PT/MyApplication";
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "MyApplication onCreate thread: " + Thread.currentThread().getName());
	}
}
