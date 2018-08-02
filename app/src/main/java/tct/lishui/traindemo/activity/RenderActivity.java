package tct.lishui.traindemo.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.adapter.MovieAdapter;
import tct.lishui.traindemo.adapter.WrapperAdapter;
import tct.lishui.traindemo.bean.TopMovieSubject;
import tct.lishui.traindemo.util.Constant;
import tct.lishui.traindemo.util.NetManager;
import tct.lishui.traindemo.util.SwipeToLoadHelper;

public class RenderActivity extends Activity implements SwipeToLoadHelper.LoadMoreListener {

	private static final String TAG = "PT/RenderActivity";
	private RecyclerView recyclerView;
	private WrapperAdapter wrapperAdapter;
	private SwipeToLoadHelper swipeToLoadHelper;
	MovieAdapter movieAdapter;
	private List<TopMovieSubject> topMovieSubjectList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_render_layout);
		initView();
		initData();

		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				LoadMovieTask loadMovieTask = new LoadMovieTask();
				loadMovieTask.execute();
			}
		});
	}

	class LoadMovieTask extends AsyncTask<Void, Void, Object> {

		public LoadMovieTask() {
		}

		@Override
		protected Object doInBackground(Void... voids) {
			return NetManager.getDouBanMovieTop(Constant.DOUBAN_MOVIE_TOP250, "0", "20", true);
		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			if (o != null){
				topMovieSubjectList = (List<TopMovieSubject>) o;
				movieAdapter.setMovies(topMovieSubjectList);
				movieAdapter.notifyDataSetChanged();
			}
		}
	}

	private void initView() {
		recyclerView = findViewById(R.id.recycler_view);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		//添加Android自带的分割线
		recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
	}

	private void initData() {
	/*	wrapperAdapter = new WrapperAdapter(new MovieAdapter(this, topMovieSubjectList));
		swipeToLoadHelper = new SwipeToLoadHelper(recyclerView, wrapperAdapter);
		swipeToLoadHelper.setLoadMoreListener(this);*/

		movieAdapter = new MovieAdapter(this, topMovieSubjectList);
		recyclerView.setAdapter(movieAdapter);
	}


	@Override
	public void onLoad() {

		Log.d(TAG, "onLoad");
	}
}
