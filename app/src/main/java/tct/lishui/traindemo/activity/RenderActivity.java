package tct.lishui.traindemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.adapter.MovieAdapter;
import tct.lishui.traindemo.adapter.WrapperAdapter;
import tct.lishui.traindemo.bean.TopMovieSubject;
import tct.lishui.traindemo.util.Constant;
import tct.lishui.traindemo.util.NetManager;
import tct.lishui.traindemo.util.SwipeToLoadHelper;

public class RenderActivity extends Activity implements SwipeToLoadHelper.LoadMoreListener{

	private static final String TAG = "PT/RenderActivity";
	private int currentItem = 0;
	private final int INTERVAL_VALUE = 5;
	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private TextView failTextView;
	private WrapperAdapter wrapperAdapter;
	private SwipeToLoadHelper swipeToLoadHelper;
	private List<TopMovieSubject> topMovieSubjectList = new ArrayList<>();
	private MovieHandler movieHandler;
	private ExecutorService mExecutor;


	private static final int INIT_MOVIE_FAIL = -1;
	private static final int INIT_MOVIE_SUCCESS = 0;
	private static final int LOAD_MOVIE_FAIL = 1;
	private static final int LOAD_MOVIE_SUCCESS = 2;
	// 处理网络失败和最后一项的行为
	private static final int INIT_TO_LOADING = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_render_layout);
		initView();
		movieHandler = new MovieHandler(this);

		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				doMovieTask(0);
			}
		});
	}


	static class MovieHandler extends Handler{
		private WeakReference<RenderActivity> renderAtyReference;
		public MovieHandler(RenderActivity renderActivity){
			renderAtyReference = new WeakReference<>(renderActivity);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (renderAtyReference == null || renderAtyReference.get() == null)
				return;

			switch (msg.what){
				case INIT_MOVIE_SUCCESS:
					renderAtyReference.get().initMovieData();
					renderAtyReference.get().updatePageNum();
					break;
				case INIT_MOVIE_FAIL:
					renderAtyReference.get().initMovieFailData();
					break;
				case LOAD_MOVIE_FAIL:
					renderAtyReference.get().loadMovieDataFail();
					sendEmptyMessageDelayed(INIT_TO_LOADING, 1000);
					break;
				case LOAD_MOVIE_SUCCESS:
					renderAtyReference.get().loadMovieData();
					renderAtyReference.get().updatePageNum();
					break;
				case INIT_TO_LOADING:
					renderAtyReference.get().resetToLoad();
					break;
			}
		}
	}

	public class MovieAcquireRunnable implements Runnable {

		int type;
		public MovieAcquireRunnable(int type){
			this.type = type;
		}
		@Override
		public void run() {
			switch (type){
				case 0:

					topMovieSubjectList = NetManager.getDouBanMovieTop(
							String.valueOf(currentItem), String.valueOf(INTERVAL_VALUE), true);
					if (topMovieSubjectList == null){
						movieHandler.obtainMessage(INIT_MOVIE_FAIL).sendToTarget();

					}else {
						movieHandler.obtainMessage(INIT_MOVIE_SUCCESS).sendToTarget();
					}
					break;
				case 1:
					if (currentItem < 250) {
						List<TopMovieSubject> tempData = NetManager.getDouBanMovieTop(
								String.valueOf(currentItem), String.valueOf(INTERVAL_VALUE), true);
						if (tempData == null) {
							movieHandler.obtainMessage(LOAD_MOVIE_FAIL).sendToTarget();
						} else {
							topMovieSubjectList.addAll(tempData);
							tempData.clear();
							movieHandler.obtainMessage(LOAD_MOVIE_SUCCESS).sendToTarget();
						}
					}
					break;
			}
		}
	}

	private void doMovieTask(int type){
		if (mExecutor == null){
			mExecutor = Executors.newSingleThreadExecutor();
		}
		mExecutor.execute(new MovieAcquireRunnable(type));
	}

	private void initView() {
		progressBar = findViewById(R.id.loading_pb);
		failTextView = findViewById(R.id.fail_tv);
		recyclerView = findViewById(R.id.recycler_view);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		//添加Android自带的分割线
		recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
	}

	private void initMovieData() {
		wrapperAdapter = new WrapperAdapter(new MovieAdapter(this, topMovieSubjectList));
		swipeToLoadHelper = new SwipeToLoadHelper(recyclerView, wrapperAdapter);
		swipeToLoadHelper.setLoadMoreListener(this);

		recyclerView.setAdapter(wrapperAdapter);

		progressBar.setVisibility(View.GONE);

	}

	private void initMovieFailData(){
		progressBar.setVisibility(View.GONE);
		failTextView.setVisibility(View.VISIBLE);
	}

	private void loadMovieData() {
		swipeToLoadHelper.setLoadMoreFinish();
		wrapperAdapter.notifyDataSetChanged();
	}

	private void resetToLoad(){
		swipeToLoadHelper.setLoadMoreFinish();

	}

	private void loadMovieDataFail(){
		swipeToLoadHelper.setLoadMoreFail(false);
	}

	private void loadMovieDataFinished(){
		swipeToLoadHelper.setLoadMoreFail(true);
	}

	private void updatePageNum(){
		currentItem = currentItem + INTERVAL_VALUE;
		Log.d(TAG, "currentItem: " + currentItem);
	}

	private void exitTask(){

		if (movieHandler != null){
			movieHandler.removeCallbacksAndMessages(null);
		}

		if (mExecutor != null && mExecutor.isShutdown()){
			mExecutor.shutdownNow();
			mExecutor = null;
		}

		if (topMovieSubjectList != null){
			topMovieSubjectList.clear();
		}

	}

	@Override
	public void onLoad() {
		doMovieTask(1);
		Log.d(TAG, "onLoad");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		exitTask();
	}
}
