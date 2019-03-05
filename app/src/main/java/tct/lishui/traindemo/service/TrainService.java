package tct.lishui.traindemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TrainService extends IntentService {

    public TrainService() {
        super("TrainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d("lishui", TrainService.class.getSimpleName() + " onHandleIntent");
        }
    }


}
