package lishui.study.activity;

import android.app.Activity;

import lishui.study.common.util.SystemUiController;

/**
 * Created by lishui.lin on 19-11-9
 */
public abstract class BaseActivity extends Activity {

    protected SystemUiController mSystemUiController;

    public SystemUiController getSystemUiController() {
        if (mSystemUiController == null) {
            mSystemUiController = new SystemUiController(getWindow());
        }
        return mSystemUiController;
    }
}
