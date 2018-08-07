package tct.lishui.traindemo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tct.lishui.traindemo.bean.HotWord;
import tct.lishui.traindemo.bean.Result;
import tct.lishui.traindemo.bean.TopMovieResult;
import tct.lishui.traindemo.bean.TopMovieSubject;
import tct.lishui.traindemo.util.Constant;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

	private OkHttpClient client = new OkHttpClient();
	@Test
	public void addition_isCorrect() {
		assertEquals(4, 2 + 2);
	}

	@Test
	public void testOkHttp(){
		String response = getDouBanMovieTop(Constant.DOUBAN_MOVIE_TOP250,"240","11",true);
		Gson gson = new Gson();
		Type resultType = new TypeToken<TopMovieResult<List<TopMovieSubject>>>() {}.getType();
		TopMovieResult<List<TopMovieSubject>> listTopMovieResult = gson.fromJson(response, resultType);
		List<TopMovieSubject> topMovieSubjects = listTopMovieResult.getSubjects();
		System.out.println("response: "+topMovieSubjects.toString());
	}

	String getDouBanMovieTop(String url, String start, String count, boolean isAdd){
		String urlStr = url;
		if (isAdd){
			urlStr = urlStr + "?startAnim=" + start + "&count=" + count;
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

		return result;
	}

	@Test
	public void testHotWord(){
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

		Result<List<HotWord>> hotWordResult = new Result<>();
		hotWordResult.setErrorCode(0);
		hotWordResult.setErrorMsg("ok");
		hotWordResult.setData(hotWords);

		Gson gson = new Gson();
		String convertJson = gson.toJson(hotWordResult);
		System.out.println("testHotWord convertJson----" + convertJson);

		Type hotwordType = new TypeToken<Result<List<HotWord>>>() {}.getType();
		Result<List<HotWord>> hotWordResultObject = gson.fromJson(convertJson, hotwordType);
		List<HotWord> hotWordList = hotWordResultObject.getData();

		System.out.println("testHotWord convertObject----" + hotWordList.get(0).toString());
	}

	@Test
	public void testJson(){
		HotWord hotWord = new HotWord();
		hotWord.setId(1);
		hotWord.setLink("http://www.baidu.com");
		hotWord.setName("TCT");
		hotWord.setOrder(2);
		hotWord.setVisible(-1);

		Gson gson = new Gson();
		String jsonStr = gson.toJson(hotWord);
		System.out.println("---"+jsonStr);

		HotWord hotWordNew = gson.fromJson(jsonStr, HotWord.class);
		System.out.println("----" + hotWordNew.getName());
	}
}