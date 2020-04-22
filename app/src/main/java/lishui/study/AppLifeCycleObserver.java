package lishui.study;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import lishui.study.receiver.NetworkReceiver;

/**
 * Created by lishui.lin on 20-4-22
 */
public class AppLifeCycleObserver implements LifecycleObserver {

    private Context mAppContext;
    private NetworkReceiver mNetworkReceiver;

    public AppLifeCycleObserver(Context appContext) {
        this.mAppContext = appContext;
    }
    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate()
    {
    }

    /**
     * 应用程序出现到前台时调用
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart()
    {
        registerReceiver();
    }

    /**
     * 应用程序出现到前台时调用
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {
    }

    /**
     * 应用程序退出到后台时调用
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause()
    {
    }

    /**
     * 应用程序退出到后台时调用
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop()
    {
        unregisterReceiver();
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     * */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy()
    {
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

}
