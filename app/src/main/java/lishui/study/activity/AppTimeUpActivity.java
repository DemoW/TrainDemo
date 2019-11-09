package lishui.study.activity;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lishui.study.common.log.LogUtil;
import lishui.study.util.Constant;
import lishui.study.R;

public class AppTimeUpActivity extends AppCompatActivity {

    private static final String TAG = "AppTimeUpActivity";
    private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_way);
		initView();
        boolean isBadStartWay = getIntent().getBooleanExtra(Constant.START_TIME_FLAG, false);
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
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//点击箭头返回
//		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				finish();
//			}
//		});
	}

	private void lazyToDo(){
		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				LogUtil.d(TAG, "lazyToDo thread 1 name: " + Thread.currentThread().getName());

				Glide.with(AppTimeUpActivity.this).load(R.drawable.alcatel_5v).into(imageView);
				// 此处不推荐直接匿名线程，只是写例子使用
				new Thread(){
					@Override
					public void run() {
						super.run();
						LogUtil.d(TAG, "lazyToDo thread 2 name: " + Thread.currentThread().getName());
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

}
