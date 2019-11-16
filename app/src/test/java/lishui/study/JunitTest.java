package lishui.study;

import androidx.collection.ArraySet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lishui.study.bean.HotWord;
import lishui.study.bean.WanResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JunitTest {

	private OkHttpClient client = new OkHttpClient();

	@Test
	public void testArraySet() {
		ArraySet<String> arraySet = new ArraySet<>();
		arraySet.add("tct.lishui.traindemo");
		arraySet.add("com.tcl.calculator");
		String mDozeDataStr = arraySet.toString();
		String[] pkgNames = mDozeDataStr.substring(1, mDozeDataStr.length() - 1).split(",");
		for (String pkgName : pkgNames) {
			System.out.println("pkgName: " + pkgName);
		}
	}

	String getDouBanMovieTop(String url, String start, String count, boolean isAdd) {
		String urlStr = url;
		if (isAdd) {
			urlStr = urlStr + "?startAnim=" + start + "&count=" + count;
		}

		Request request = new Request.Builder()
				.url(urlStr)
				.build();

		System.out.println("request: " + request.toString());
		String result = "";
		try {
			okhttp3.Response response = client.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Test
	public void testHotWord() {
		HotWord hotWord = new HotWord();
		hotWord.setId(1);
		hotWord.setLink("http://www.baidu.com");
		hotWord.setName("TCT");
		hotWord.setOrder(2);
		hotWord.setVisible(-1);

		HotWord hotWord2 = new HotWord();
		hotWord2.setId(2);
		hotWord2.setLink("http://www.tcl.com");
		hotWord2.setName("TCL");
		hotWord2.setOrder(3);
		hotWord2.setVisible(1);

		List<HotWord> hotWords = new ArrayList<>();
		hotWords.add(hotWord);
		hotWords.add(hotWord2);

		WanResult<List<HotWord>> hotWordResult = new WanResult<>();
		hotWordResult.setErrorCode(0);
		hotWordResult.setErrorMsg("ok");
		hotWordResult.setData(hotWords);

		Gson gson = new Gson();
		String convertJson = gson.toJson(hotWordResult);
		System.out.println("testHotWord convertJson----" + convertJson);

		Type hotwordType = new TypeToken<WanResult<List<HotWord>>>() {
		}.getType();
		WanResult<List<HotWord>> hotWordResultObject = gson.fromJson(convertJson, hotwordType);
		List<HotWord> hotWordList = hotWordResultObject.getData();

		System.out.println("testHotWord convertObject----" + hotWordList.get(0).toString());
	}

	@Test
	public void testJson() {
		HotWord hotWord = new HotWord();
		hotWord.setId(1);
		hotWord.setLink("http://www.baidu.com");
		hotWord.setName("TCT");
		hotWord.setOrder(2);
		hotWord.setVisible(-1);

		Gson gson = new Gson();
		String jsonStr = gson.toJson(hotWord);
		System.out.println("---" + jsonStr);

		HotWord hotWordNew = gson.fromJson(jsonStr, HotWord.class);
		System.out.println("----" + hotWordNew.getName());
	}
}