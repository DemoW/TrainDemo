package lishui.study.viewmodel;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import lishui.study.http.NetworkConstant;

/**
 * Created by lishui.lin on 20-4-22
 */
public class MainSharedViewModel extends AndroidViewModel {

    private Context mAppContext;
    private LocalSharedReceiver mLocalSharedReceiver;

    private MutableLiveData<String> mNetworkState = new MutableLiveData<>();

    public MainSharedViewModel(@NonNull Application application) {
        super(application);
        mAppContext = application.getApplicationContext();
        registerReceiver();
    }

    public LiveData<String> getNetworkLiveState() {
        return mNetworkState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        unregisterReceiver();
    }

    private class LocalSharedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra(NetworkConstant.LOCAL_NETWORK_STATE);
            if (!TextUtils.isEmpty(state)) {
                mNetworkState.postValue(state);
            }
        }
    }

    private void registerReceiver() {
        if (mLocalSharedReceiver == null) {
            mLocalSharedReceiver = new LocalSharedReceiver();
            IntentFilter filter = new IntentFilter(NetworkConstant.LOCAL_NETWORK_STATE_ACTION);
            LocalBroadcastManager.getInstance(mAppContext).registerReceiver(mLocalSharedReceiver, filter);
        }
    }

    private void unregisterReceiver() {
        if (mLocalSharedReceiver != null) {
            LocalBroadcastManager.getInstance(mAppContext).unregisterReceiver(mLocalSharedReceiver);
            mLocalSharedReceiver = null;
        }
    }
}
