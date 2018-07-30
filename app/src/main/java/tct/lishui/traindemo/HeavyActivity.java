package tct.lishui.traindemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tct.lishui.traindemo.util.Constant;
import tct.lishui.traindemo.util.NetManager;

public class HeavyActivity extends Activity {

	private TextView resultTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heavy);
		resultTv = findViewById(R.id.response_tv);

		MyTask myTask = new MyTask(this);
		myTask.execute("157.122.116.172");
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
