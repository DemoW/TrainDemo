package lishui.study.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static lishui.study.util.NetConstant.WANANDROID_BASE_URL;

/**
 * Created by lishui.lin on 19-11-13
 */
public class RetrofitManager {

    private static RetrofitManager retrofitManager;
    private WanAndroidService mWanAndroidService;

    private RetrofitManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WANANDROID_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mWanAndroidService = retrofit.create(WanAndroidService.class);
    }

    public synchronized static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }

    public WanAndroidService getWanAndroidService() {
        return mWanAndroidService;
    }
}
