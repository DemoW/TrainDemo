package tct.lishui.traindemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tct.lishui.traindemo.bean.Banner;
import tct.lishui.traindemo.bean.Result;
import tct.lishui.traindemo.bean.TopMovieResult;
import tct.lishui.traindemo.bean.TopMovieSubject;

/**
 * Created by lishui.lin on 18-7-30 09:31
 */
public class NetManager {
	private static final String TAG = "PT/NetManager";

	public static List<Banner> requestBanner(){
		HttpURLConnection httpURLConnection = null;
        List<Banner> banners = null;
		try {
			URL url = new URL(Constant.BANNER_URL_STR);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setDoInput(true);

			int errorCode = httpURLConnection.getResponseCode();
			if (errorCode == HttpURLConnection.HTTP_OK){
				String jsonStr = convertStreamToString(httpURLConnection.getInputStream());
				Gson gson = new Gson();
				Type bannerType = new TypeToken<Result<List<Banner>>>() {}.getType();
				Result<List<Banner>> bannerResultObject = gson.fromJson(jsonStr, bannerType);
                banners = bannerResultObject.getData();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpURLConnection.disconnect();
		}
		return banners;
	}
	public static String netRequest(String requestParam){

		HttpURLConnection httpURLConnection = null;
		String response = "";
		try {
			// 223.74.197.184
//			String requestUrl = Constant.URL_STR + "?ip=" + requestParam;
			String requestUrl = Constant.HOT_WORD_URL_STR;
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

	private static String convertStreamToString(InputStream is) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null){
//			sb.append(line + "\r\n");
			sb.append(line);
		}
		bufferedReader.close();
		String response = sb.toString();
		return response;
	}

	/**
	 * 判断是否有网络连接
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			// 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
					.CONNECTIVITY_SERVICE);
			// 获取NetworkInfo对象
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			//判断NetworkInfo对象是否为空
			if (networkInfo != null)
				return networkInfo.isAvailable();
		}
		return false;
	}

	public static List<TopMovieSubject> getDouBanMovieTop(String url, String start, String count, boolean isAdd){
		OkHttpClient client = new OkHttpClient();
		List<TopMovieSubject> topMovieSubjects = null;
		String urlStr = url;
		if (isAdd){
			urlStr = urlStr + "?start=" + start + "&count=" + count;
		}

		Request request = new Request.Builder()
				.url(urlStr)
				.build();

		System.out.println("request: " + request.toString());
		String result = "";
		try {
			Response response = client.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		Type resultType = new TypeToken<TopMovieResult<List<TopMovieSubject>>>() {}.getType();
		TopMovieResult<List<TopMovieSubject>> listTopMovieResult = gson.fromJson(result, resultType);
		topMovieSubjects = listTopMovieResult.getSubjects();
		return topMovieSubjects;
	}

}
