package lishui.study;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import lishui.study.bean.OAChapter;
import lishui.study.bean.WanArticleResult;
import lishui.study.http.RetrofitManager;
import lishui.study.http.WanResultCallback;
import lishui.study.bean.BannerInfo;
import lishui.study.bean.WanArticle;
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
    public LiveData<List<WanArticle>> getSquareData(int page) {
        MutableLiveData<List<WanArticle>> mutableLiveData = new MutableLiveData<>();

        RetrofitManager.getInstance()
                .getWanAndroidService()
                .listSquareData(page)
                .enqueue(new WanResultCallback<WanArticleResult<List<WanArticle>>>() {
                    @Override
                    public void onResponse(WanArticleResult<List<WanArticle>> listArticleResult) {
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

    // 获取公众号列表
    public LiveData<List<OAChapter>> getChapterData() {
        MutableLiveData<List<OAChapter>> mutableLiveData = new MutableLiveData<>();

        RetrofitManager.getInstance()
                .getWanAndroidService()
                .listWxChapter()
                .enqueue(new WanResultCallback<List<OAChapter>>() {
                    @Override
                    public void onResponse(List<OAChapter> oaChapters) {
                        mutableLiveData.postValue(oaChapters);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mutableLiveData.postValue(null);
                        LogUtil.w(TAG, "getChapterData onFailure", throwable);
                    }
                });
        return mutableLiveData;
    }

    // 获取某个公众号的历史数据
    public LiveData<List<WanArticle>> updateWanArticleData(MutableLiveData<List<WanArticle>> mutableLiveData, int id, int page) {
//        MutableLiveData<List<WanArticle>> mutableLiveData = new MutableLiveData<>();

        RetrofitManager.getInstance()
                .getWanAndroidService()
                .listWxArticle(id, page)
                .enqueue(new WanResultCallback<WanArticleResult<List<WanArticle>>>() {
                    @Override
                    public void onResponse(WanArticleResult<List<WanArticle>> listWanArticleResult) {
                        if (listWanArticleResult != null && listWanArticleResult.getDatas() != null) {
                            // LogUtil.d("updateWanArticleData: " + listWanArticleResult.getDatas());

                            List<WanArticle> wanArticles = mutableLiveData.getValue();
                            List<WanArticle> tempArticles = listWanArticleResult.getDatas();
                            if (wanArticles != null && !wanArticles.isEmpty()) {

                                if (tempArticles != null
                                        && wanArticles.get(0).getChapterId() == tempArticles.get(0).getChapterId()) {
                                    wanArticles.addAll(tempArticles);
                                    mutableLiveData.postValue(wanArticles);
                                } else {
                                    mutableLiveData.postValue(tempArticles);
                                }
                            } else {
                                mutableLiveData.postValue(tempArticles);
                            }
                        } else {
                            mutableLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        List<WanArticle> wanArticles = mutableLiveData.getValue();
                        if (wanArticles == null || wanArticles.isEmpty()) {
                            mutableLiveData.postValue(null);
                        }
                        LogUtil.w(TAG, "updateWanArticleData onFailure in id: " + id + ", page: " + page, throwable);
                    }
                });

        return mutableLiveData;
    }
}
