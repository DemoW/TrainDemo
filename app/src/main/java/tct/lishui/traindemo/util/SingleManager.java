package tct.lishui.traindemo.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by lishui.lin on 18-8-7 14:30
 * 由于这个单例模式并没有执行耗时操作，但是通过adb查看meminfo,会发现多了一个Activity引用
 */
public class SingleManager {

	private static final String TAG = "PT/SingleManager";
	private Context mContext;
	private static SingleManager singleManager;
	private SingleManager(Context context){
		this.mContext = context;
	}

	// leak in single mode
	public static SingleManager getInstance(Context context){
		if (singleManager == null){
			singleManager = new SingleManager(context);
		}

		return singleManager;
	}

	// not leak in single mode
	public static SingleManager getInstance2(Context context){
		if (singleManager == null){
			singleManager = new SingleManager(context.getApplicationContext());
		}

		return singleManager;
	}

	public void printWord(boolean isLeak){
		if (isLeak){
			Log.d(TAG, "I am running leak from SingleManager. ");
		}else {
			Log.d(TAG, "I am not running leak from SingleManager. ");

		}
	}
}
