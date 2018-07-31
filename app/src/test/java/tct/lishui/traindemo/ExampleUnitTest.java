package tct.lishui.traindemo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tct.lishui.traindemo.bean.HotWord;
import tct.lishui.traindemo.bean.Result;
import tct.lishui.traindemo.util.NetManager;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

	@Test
	public void addition_isCorrect() {
		assertEquals(4, 2 + 2);
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