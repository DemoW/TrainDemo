package tct.lishui.traindemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

	private Context mContext;
	@Before
	public void setUp(){
		mContext = InstrumentationRegistry.getTargetContext();
	}
	@Test
	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("tct.lishui.traindemo", appContext.getPackageName());
	}

	@Test
	public void testSharedPreference(){
		System.out.println("----testSharedPreference");
		// 默认名称：tct.lishui.traindemo_preferences.xml
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		editor.putString("water", "now is keep in sp");
		editor.apply();
	}
}
