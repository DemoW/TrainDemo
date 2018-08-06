package tct.lishui.traindemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.adapter.BannerAdapter;
import tct.lishui.traindemo.bean.Banner;
import tct.lishui.traindemo.util.Constant;
import tct.lishui.traindemo.util.NetManager;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "PT/MainActivity";
	private TextView loading_tv;
	private RecyclerView recyclerView;
	private List<Banner> bannerList = new ArrayList<>();
	private BannerAdapter bannerAdapter;
	private boolean isReceiverDo = false;
	private boolean isInitialBad = false;
	private boolean isInitialGood = false;

	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				if (NetManager.isNetworkConnected(context)) {
					if (isReceiverDo) {
						Log.d(TAG, "-----NetworkConnected");
						BannerDataTask bannerDataTask = new BannerDataTask(MainActivity.this);
						bannerDataTask.execute();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// initial the view
		initView();
		initRecyclerView();
		lazyToDo();

	}

	@Override
	protected void onStart() {
		super.onStart();
		registerNetworkListener();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterNetworkListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.start_quick_time) {

			Intent intent = new Intent(this, AppTimeUpActivity.class);
			intent.putExtra(Constant.START_TIME_FLAG, false);
			startActivity(intent);

		} else if (itemId == R.id.slow_start_time) {

			Intent intent = new Intent(this, AppTimeUpActivity.class);
			intent.putExtra(Constant.START_TIME_FLAG, true);
			startActivity(intent);

		} else if (itemId == R.id.launch_heavy_aty) {

			Intent intent = new Intent(this, RenderActivity.class);
			startActivity(intent);
		} else if (itemId == R.id.launch_leak_aty) {

			Intent intent = new Intent(this, LeakActivity.class);
			startActivity(intent);
		}
		return true;
	}

	private void initRecyclerView() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		bannerAdapter = new BannerAdapter(this, bannerList);
		recyclerView.setAdapter(bannerAdapter);
	}

	private void initView(){
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		recyclerView = findViewById(R.id.recycler_view);
		loading_tv = findViewById(R.id.loading_text);
	}


	private void lazyToDo() {
		getWindow().getDecorView().postDelayed(new Runnable() {
			@Override
			public void run() {
				BannerDataTask bannerDataTask = new BannerDataTask(MainActivity.this);
				bannerDataTask.execute();
			}
		}, 500);
	}

	private void registerNetworkListener(){
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, intentFilter);
	}

	private void unregisterNetworkListener(){
		if (networkReceiver != null){
			unregisterReceiver(networkReceiver);
		}
	}
	static class BannerDataTask extends AsyncTask<Void, Void, Object>{

		private WeakReference<MainActivity> mainActivityWeakReference = null;
		public BannerDataTask(MainActivity mainActivity){
			mainActivityWeakReference = new WeakReference<>(mainActivity);
		}

		@Override
		protected Object doInBackground(Void... voids) {
			return NetManager.requestBanner();
		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if (mainActivityWeakReference != null && mainActivityWeakReference.get() != null){
				if (o == null) {
					if (!mainActivityWeakReference.get().isInitialBad) {
						// 初始化Banner数据，无网络也能显示
						Banner tempBanner = new Banner();
						tempBanner.setTitle("别着急，再等等啦~");
						tempBanner.setUrl("");
						tempBanner.setImagePath("");
						mainActivityWeakReference.get().bannerAdapter.clearBanner();
						mainActivityWeakReference.get().bannerList.add(tempBanner);
						mainActivityWeakReference.get().bannerAdapter.notifyDataSetChanged();

						mainActivityWeakReference.get().isInitialBad = true;
						mainActivityWeakReference.get().isReceiverDo = true;
						mainActivityWeakReference.get().loading_tv.setVisibility(View.GONE);

					}
				}else {
					if (!mainActivityWeakReference.get().isInitialGood) {
						mainActivityWeakReference.get().bannerAdapter.clearBanner();
						mainActivityWeakReference.get().bannerAdapter.setDataSet((List<Banner>) o);
						mainActivityWeakReference.get().bannerAdapter.notifyDataSetChanged();

						mainActivityWeakReference.get().isInitialGood = true;
						mainActivityWeakReference.get().isReceiverDo = false;
						mainActivityWeakReference.get().loading_tv.setVisibility(View.GONE);
					}
				}
			}
		}
	}

}
