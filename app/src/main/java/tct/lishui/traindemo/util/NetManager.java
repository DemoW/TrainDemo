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
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Constant.BANNER_URL_STR)
				.build();

		String result = "";
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				result = response.body().string();
			}else {
				result = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Banner> banners = null;
		if (!result.isEmpty()){
			Gson gson = new Gson();
			Type bannerType = new TypeToken<Result<List<Banner>>>() {}.getType();
			Result<List<Banner>> bannerResultObject = gson.fromJson(result, bannerType);
			banners = bannerResultObject.getData();
		}

		return banners;
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

	public static List<TopMovieSubject> getDouBanMovieTop(String start, String count, boolean isAdd){
		OkHttpClient client = new OkHttpClient();
		List<TopMovieSubject> topMovieSubjects = null;
		String urlStr = Constant.DOUBAN_MOVIE_TOP250;
		if (isAdd){
			urlStr = urlStr + "?start=" + start + "&count=" + count;
		}

		Request request = new Request.Builder()
				.url(urlStr)
				.build();

		String result = "";
		try {
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				result = response.body().string();
			}else {
				result = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!result.isEmpty()){
			Gson gson = new Gson();
			Type resultType = new TypeToken<TopMovieResult<List<TopMovieSubject>>>() {
			}.getType();
			TopMovieResult<List<TopMovieSubject>> listTopMovieResult = gson.fromJson(result, resultType);
			topMovieSubjects = listTopMovieResult.getSubjects();
		}

		return topMovieSubjects;
	}

}
