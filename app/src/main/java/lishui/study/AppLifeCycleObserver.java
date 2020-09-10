package lishui.study;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import lishui.study.common.log.LogUtils;
import lishui.study.receiver.NetworkReceiver;

/**
 * Created by lishui.lin on 20-4-22
 */
public class AppLifeCycleObserver implements LifecycleEventObserver {

    private Context mAppContext;
    private NetworkReceiver mNetworkReceiver;

    public AppLifeCycleObserver(Context appContext) {
        this.mAppContext = appContext;
    }

    private void registerReceiver() {
        if (mNetworkReceiver == null) {
            mNetworkReceiver = new NetworkReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mAppContext.registerReceiver(mNetworkReceiver, filter);
        }
    }

    private void unregisterReceiver() {
        if (mNetworkReceiver != null) {
            mAppContext.unregisterReceiver(mNetworkReceiver);
            mNetworkReceiver = null;
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        // ON_DESTROY在应用内是调用不到的
        LogUtils.d(false, "event=" + event.name());
        if (event == Lifecycle.Event.ON_START) {
            registerReceiver();
        } else if (event == Lifecycle.Event.ON_STOP) {
            unregisterReceiver();
        }
    }
}
