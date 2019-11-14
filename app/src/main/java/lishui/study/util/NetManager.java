package lishui.study.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import lishui.study.bean.BannerInfo;
import lishui.study.bean.WanResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import lishui.study.bean.TopMovieResult;
import lishui.study.bean.TopMovieSubject;

/**
 * Created by lishui.lin on 18-7-30 09:31
 */
public class NetManager {

	public static List<BannerInfo> requestBanner(){
        OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder()
				.url(NetConstant.BANNER_URL_STR)
				.build();

		String result = "";
		try {
			okhttp3.Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				result = response.body().string();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<BannerInfo> banners = null;
		if (!result.isEmpty()){
			Gson gson = new Gson();
			Type bannerType = new TypeToken<WanResult<List<BannerInfo>>>() {}.getType();
			WanResult<List<BannerInfo>> bannerResultObject = gson.fromJson(result, bannerType);
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
		String urlStr = NetConstant.DOUBAN_MOVIE_TOP250;
		if (isAdd){
			urlStr = urlStr + "?start=" + start + "&count=" + count;
		}

		Request request = new Request.Builder()
				.url(urlStr)
				.build();

		String result = "";
		try {
			okhttp3.Response response = client.newCall(request).execute();
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
