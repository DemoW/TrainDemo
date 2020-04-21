package lishui.study.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lishui.study.DataRepository;
import lishui.study.bean.BannerInfo;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<BannerInfo>> mBannerLiveList = new MutableLiveData<>();

    public HomeViewModel() {
    }

    public LiveData<List<BannerInfo>> getBannerLiveList() {
        return mBannerLiveList;
    }

    public void loadBannerDataIfNeed() {
        DataRepository.getInstance().loadBannerData(new DataRepository.DataReceiver<List<BannerInfo>>() {
            @Override
            public void onDataLoaded(List<BannerInfo> data) {
                mBannerLiveList.postValue(data);
            }
        });
    }

}
