package tct.lishui.traindemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tct.lishui.traindemo.util.Constant;

public class HeavyActivity extends Activity {

	private TextView resultTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heavy);
		resultTv = findViewById(R.id.response_tv);

		MyTask myTask = new MyTask();
		myTask.execute("223.74.197.184");
	}

	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... strings) {
			return netRequest(strings[0]);
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			resultTv.setText(s);
		}
	}

	private String netRequest(String requestParam){
		HttpURLConnection httpURLConnection = null;
		String response = "";
		try {
			// 223.74.197.184
			String requestUrl = Constant.URL_STR + "?ip=" + requestParam;
			URL url = new URL(requestUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(30000);
			httpURLConnection.setReadTimeout(30000);
			httpURLConnection.setDoInput(true);

			int errorCode = httpURLConnection.getResponseCode();

			if (errorCode == HttpURLConnection.HTTP_OK){
				response = convertStreamToString(httpURLConnection.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpURLConnection.disconnect();
		}
		return response;
	}

	private String convertStreamToString(InputStream is) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null){
			sb.append(line + "\n");
		}

		String response = sb.toString();
		return response;
	}
}
