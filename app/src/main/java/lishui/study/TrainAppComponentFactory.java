package lishui.study;

import android.app.AppComponentFactory;
import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import lishui.study.common.log.LogUtils;

/**
 * Created by lishui.lin on 20-7-25
 *
 * 组件的初始化工厂,可对四大组件及其Application执行初始化操作
 */
@RequiresApi(api = Build.VERSION_CODES.P)
public class TrainAppComponentFactory extends AppComponentFactory {

    public TrainAppComponentFactory() {
        super();
    }

    @NonNull
    @Override
    public Application instantiateApplication(@NonNull ClassLoader cl, @NonNull String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        LogUtils.d("instantiateApplication className=" + className);
        return super.instantiateApplication(cl, className);
    }
}
