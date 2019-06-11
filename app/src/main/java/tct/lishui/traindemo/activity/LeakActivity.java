package tct.lishui.traindemo.activity;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.util.SingleManager;

public class LeakActivity extends AppCompatActivity {

	private static final String TAG = "PT/LeakActivity";
	private static final int NOT_LEAK_HANDLER_MSG = 1;

	private MyHandler myHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leak);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setTitle("LeakActivity");
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		doLeakWork();

//		doNotLeakWork();

	}

	private void doLeakWork() {
		SingleManager singleManager = SingleManager.getInstance(this);
		singleManager.printWord(true);

		// 匿名Handler执行延时任务
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, Thread.currentThread().getName() + ": doing a delay work but leak now");
			}
		}, 1000 * 60 * 60);

	}

	private void doNotLeakWork(){
		SingleManager singleManager = SingleManager.getInstance2(this);
		singleManager.printWord(false);

		myHandler = new MyHandler(this);
		myHandler.sendEmptyMessageDelayed(NOT_LEAK_HANDLER_MSG, 1000 * 60 * 60);


	}

	static class MyHandler extends Handler{

		private WeakReference<LeakActivity> leakActivityWeakReference;

		public MyHandler(LeakActivity leakActivity){
			leakActivityWeakReference = new WeakReference<>(leakActivity);
		}
		@Override
		public void handleMessage(Message msg) {
			if (leakActivityWeakReference != null && leakActivityWeakReference.get() != null){
				switch (msg.what){
					case NOT_LEAK_HANDLER_MSG:
						Log.d(TAG, Thread.currentThread().getName() + ": doing a delay work but not leak. ");
						break;
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "LeakActivity onDestroy");
		if (myHandler != null){
//			myHandler.removeMessages(NOT_LEAK_HANDLER_MSG);
			myHandler.removeCallbacksAndMessages(null);
		}
	}
}
