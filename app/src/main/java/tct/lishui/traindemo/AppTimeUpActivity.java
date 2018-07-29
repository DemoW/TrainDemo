package tct.lishui.traindemo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import tct.lishui.traindemo.util.Constant;

public class AppTimeUpActivity extends AppCompatActivity {

	private static final String TAG = "PT/AppTimeUpActivity";
	private boolean isBadStartWay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_time);
		isBadStartWay = getIntent().getBooleanExtra(Constant.START_TIME_FLAG, false);
		if (isBadStartWay){
			oneExcessiveWork();
		}
//		lazyToDo();

	}

	private void lazyToDo(){
		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "lazyToDo thread 1 name: " + Thread.currentThread().getName());
				// 此处不推荐直接匿名线程，只是写例子使用
				new Thread(){
					@Override
					public void run() {
						super.run();
						Log.d(TAG, "lazyToDo thread 2 name: " + Thread.currentThread().getName());
						oneExcessiveWork();
					}
				}.start();
			}
		});
	}

	// leak work, when define a large variable and it not be released
//	List<String> stringList = null;
	private void oneExcessiveWork(){
		List<String> stringList = new ArrayList<>();
//		stringList = new ArrayList<>();
		for (int i = 0; i < 100000; i++){
			stringList.add("str" + i);
		}

		// should clear while it not use
		stringList.clear();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
