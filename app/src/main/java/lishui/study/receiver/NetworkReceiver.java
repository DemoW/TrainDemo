package lishui.study.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import lishui.study.common.log.LogUtils;
import lishui.study.http.NetworkConstant;

/**
 * Created by lishui.lin on 20-4-22
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn =  (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            sendLocalBroadcast(context, NetworkConstant.NETWORK_WIFI);
            LogUtils.d("networkInfo wifi");
        } else if (networkInfo != null) {
            sendLocalBroadcast(context, NetworkConstant.NETWORK_ANY);
            LogUtils.d("networkInfo available");

        } else {
            sendLocalBroadcast(context, NetworkConstant.NETWORK_DISABLE);
            // there is no network connection (mobile or Wi-Fi)
            LogUtils.d("networkInfo invalid");
        }
    }

    private void sendLocalBroadcast(Context context, String netState) {
        Intent intent = new Intent(NetworkConstant.LOCAL_NETWORK_STATE_ACTION);
        intent.putExtra(NetworkConstant.LOCAL_NETWORK_STATE, netState);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
