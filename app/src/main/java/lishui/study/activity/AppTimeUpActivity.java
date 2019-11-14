package lishui.study.activity;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lishui.study.R;
import lishui.study.common.log.LogUtil;
import lishui.study.common.util.Utilities;
import lishui.study.util.NetConstant;

public class AppTimeUpActivity extends BaseActivity {

    private static final String TAG = "AppTimeUpActivity";
    private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_app_time);
		initView();
        boolean isBadStartWay = getIntent().getBooleanExtra(NetConstant.START_TIME_FLAG, false);
		if (isBadStartWay){
	/*		new Handler().post(new Runnable() {
				@Override
				public void run() {
					ImagerResizer imagerResizer = new ImagerResizer();
					Bitmap bitmap = imagerResizer.decodeSampledBitmapFromResource(getResources(),
							R.drawable.alcatel_5v, imageView.getWidth(), imageView.getHeight());
					imageView.setImageBitmap(bitmap);
				}
			});*/

			imageView.setImageResource(R.drawable.alcatel_5v);
			oneExcessiveWork();
		}else {
			lazyToDo();
		}
	}

	private void initView() {
		imageView = findViewById(R.id.toolbar_bg);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
		//点击箭头返回
//		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				finish();
//			}
//		});
	}

	private void lazyToDo(){
        Glide.with(AppTimeUpActivity.this).load(R.drawable.alcatel_5v).into(imageView);
        Utilities.THREAD_POOL_EXECUTOR.execute(()->{
            LogUtil.d(TAG, "lazyToDo thread loading and its name: " + Thread.currentThread().getName());
            oneExcessiveWork();
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

}
