package tct.lishui.traindemo.util;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tct.lishui.traindemo.bean.Banner;

/**
 * Created by lishui.lin on 18-7-30 09:31
 */
public class NetManager {

	public static String testJson(){
		HttpURLConnection httpURLConnection = null;
		String response = "";
		try {
			URL url = new URL(Constant.BANNER_URL_STR);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(30000);
			httpURLConnection.setReadTimeout(30000);
			httpURLConnection.setDoInput(true);

			int errorCode = httpURLConnection.getResponseCode();
			if (errorCode == HttpURLConnection.HTTP_OK){
				String jsonStr = convertStreamToString(httpURLConnection.getInputStream());
				String jsStr = "{\"data\":[{\"desc\":\"\",\"id\":6,\"imagePath\":\"http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png\",\"isVisible\":1,\"order\":1,\"title\":\"我们新增了一个常用导航Tab~\",\"type\":0,\"url\":\"http://www.wanandroid.com/navi\"},{\"desc\":\"一起来做个App吧\",\"id\":10,\"imagePath\":\"http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\"isVisible\":1,\"order\":1,\"title\":\"一起来做个App吧\",\"type\":0,\"url\":\"http://www.wanandroid.com/blog/show/2\"},{\"desc\":\"\",\"id\":7,\"imagePath\":\"http://www.wanandroid.com/blogimgs/ffb61454-e0d2-46e7-bc9b-4f359061ae20.png\",\"isVisible\":1,\"order\":2,\"title\":\"送你一个暖心的Mock API工具\",\"type\":0,\"url\":\"http://www.wanandroid.com/blog/show/10\"},{\"desc\":\"\",\"id\":4,\"imagePath\":\"http://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png\",\"isVisible\":1,\"order\":0,\"title\":\"看看别人的面经，搞定面试~\",\"type\":1,\"url\":\"http://www.wanandroid.com/article/list/0?cid=73\"},{\"desc\":\"\",\"id\":3,\"imagePath\":\"http://www.wanandroid.com/blogimgs/fb0ea461-e00a-482b-814f-4faca5761427.png\",\"isVisible\":1,\"order\":1,\"title\":\"兄弟，要不要挑个项目学习下?\",\"type\":1,\"url\":\"http://www.wanandroid.com/project\"},{\"desc\":\"加个友情链接吧~\",\"id\":11,\"imagePath\":\"http://www.wanandroid.com/blogimgs/84810df6-adf1-45bc-b3e2-294fa4e95005.png\",\"isVisible\":1,\"order\":1,\"title\":\"加个友情链接吧~\",\"type\":1,\"url\":\"http://www.wanandroid.com/ulink\"},{\"desc\":\"\",\"id\":2,\"imagePath\":\"http://www.wanandroid.com/blogimgs/90cf8c40-9489-4f9d-8936-02c9ebae31f0.png\",\"isVisible\":1,\"order\":2,\"title\":\"JSON工具\",\"type\":1,\"url\":\"http://www.wanandroid.com/tools/bejson\"},{\"desc\":\"\",\"id\":5,\"imagePath\":\"http://www.wanandroid.com/blogimgs/acc23063-1884-4925-bdf8-0b0364a7243e.png\",\"isVisible\":1,\"order\":3,\"title\":\"微信文章合集\",\"type\":1,\"url\":\"http://www.wanandroid.com/blog/show/6\"}],\"errorCode\":0,\"errorMsg\":\"\"}";
				Log.d("water", "jsonStr: " + jsonStr);
				Gson gson = new Gson();
//				Banner banner = gson.fromJson(jsonStr, Banner.class);
				Banner banner = gson.fromJson(jsStr, Banner.class);
//				List<HotWord> hotWords = gson.fromJson(jsonStr, new TypeToken<List<HotWord>>() {}.getType());
				Log.d("water", "my_json: " + banner.getErrorCode());
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
	public static String netRequest(String requestParam){
		HttpURLConnection httpURLConnection = null;
		String response = "";
		try {
			// 223.74.197.184
//			String requestUrl = Constant.URL_STR + "?ip=" + requestParam;
			String requestUrl = Constant.WAN_ANDROID_URL_STR;
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
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
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
}
