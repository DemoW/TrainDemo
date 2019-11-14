package lishui.study.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lishui.study.DataRepository;
import lishui.study.bean.BannerInfo;

public class MainViewModel extends ViewModel {

    private LiveData<List<BannerInfo>> mBannerLiveList;

    public MainViewModel() {
        mBannerLiveList = DataRepository.getInstance().getBannerData();
        DataRepository.getInstance().getSquareData(1);
    }

    public LiveData<List<BannerInfo>> getBannerLiveList() {
        return mBannerLiveList;
    }

    public void loadBannerDataIfNeed() {

//        Utilities.THREAD_POOL_EXECUTOR.execute(()->{
//            List<BannerInfo> banners = NetManager.requestBanner();
//            mBannerLiveList.postValue(banners);
//        });
    }

}
