package tct.lishui.traindemo.util;

import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tct.lishui.traindemo.bean.Banner;
import tct.lishui.traindemo.bean.HotWord;
import tct.lishui.traindemo.bean.Result;

/**
 * Created by lishui.lin on 18-7-30 09:31
 */
public class NetManager {

	public static String testJson(){
		HttpURLConnection httpURLConnection = null;
		String response = "";
		try {
			URL url = new URL(Constant.HOT_WORD_URL_STR);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(30000);
			httpURLConnection.setReadTimeout(30000);
			httpURLConnection.setDoInput(true);

			int errorCode = httpURLConnection.getResponseCode();
			if (errorCode == HttpURLConnection.HTTP_OK){
				String jsonStr = convertStreamToString(httpURLConnection.getInputStream());
				Gson gson = new Gson();

				Type hotwordType = new TypeToken<Result<List<HotWord>>>() {}.getType();
				Result<List<HotWord>> hotWordResultObject = gson.fromJson(jsonStr, hotwordType);
				List<HotWord> hotWordList = hotWordResultObject.getData();
				System.out.println("hotWordList: "+ hotWordList.toString());
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
	 *将输入流转化成字符串
	 * inputStream 指定的输入流
	 */
	public static String getStringFromInputStream(InputStream inputStream) {
		String resultData = null;      //需要返回的结果
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultData = new String(byteArrayOutputStream.toByteArray());
		return resultData;
	}
}
