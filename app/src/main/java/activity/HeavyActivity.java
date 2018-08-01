package activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.util.NetManager;

public class HeavyActivity extends Activity {

	private TextView resultTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heavy);
		resultTv = findViewById(R.id.response_tv);

		MyTask myTask = new MyTask(this);
		myTask.execute("223.74.197.184");
	}

	static class MyTask extends AsyncTask<String, Void, String> {

		private WeakReference<HeavyActivity> heavyActivityWeakReference = null;
		MyTask (HeavyActivity heavyActivity){
			heavyActivityWeakReference = new WeakReference<>(heavyActivity);
		}

		@Override
		protected String doInBackground(String... strings) {
			return NetManager.netRequest(strings[0]);
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (heavyActivityWeakReference != null && heavyActivityWeakReference.get() != null){
				heavyActivityWeakReference.get().resultTv.setText(s);
			}
		}
	}


}
