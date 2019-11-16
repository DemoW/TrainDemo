package lishui.study.ui;

import android.app.Activity;

import lishui.demo.base_ui.util.SystemUiController;


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
