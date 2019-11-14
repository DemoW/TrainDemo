package lishui.study;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import lishui.study.http.RetrofitManager;
import lishui.study.http.WanResultCallback;
import lishui.study.bean.ArticleResult;
import lishui.study.bean.BannerInfo;
import lishui.study.bean.SquareInfo;
import lishui.study.common.log.LogUtil;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static final String TAG = "DataRepository";

    private static DataRepository sInstance;

    private DataRepository() {

    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    // 获取WanAndroid的Banner轮播图数据
    public LiveData<List<BannerInfo>> getBannerData() {
        MutableLiveData<List<BannerInfo>> mutableLiveData = new MutableLiveData<>();
        RetrofitManager.getInstance()
                .getWanAndroidService()
                .listBanner()
                .enqueue(new WanResultCallback<List<BannerInfo>>() {
                    @Override
                    public void onResponse(List<BannerInfo> banners) {
                        mutableLiveData.postValue(banners);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mutableLiveData.postValue(null);
                        LogUtil.w(TAG, "getBannerData onFailure", throwable);
                    }
                });

        return mutableLiveData;
    }

    // 获取WanAndroid的广场数据
    public LiveData<List<SquareInfo>> getSquareData(int page) {
        MutableLiveData<List<SquareInfo>> mutableLiveData = new MutableLiveData<>();

        RetrofitManager.getInstance()
                .getWanAndroidService()
                .listSquareData(page)
                .enqueue(new WanResultCallback<ArticleResult<List<SquareInfo>>>() {
                    @Override
                    public void onResponse(ArticleResult<List<SquareInfo>> listArticleResult) {
                        if (listArticleResult != null && listArticleResult.getDatas() != null) {
                            LogUtil.d("getSquareData: " + listArticleResult.getDatas());
                            mutableLiveData.postValue(listArticleResult.getDatas());
                        } else {
                            mutableLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mutableLiveData.postValue(null);
                        LogUtil.w(TAG, "getSquareData onFailure", throwable);
                    }
                });

        return mutableLiveData;
    }
}
