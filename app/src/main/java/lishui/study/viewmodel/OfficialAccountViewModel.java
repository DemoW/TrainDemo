package lishui.study.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lishui.study.DataRepository;
import lishui.study.bean.OAChapter;
import lishui.study.bean.WanArticle;

/**
 * TODO:
 * 1. 数据已经刷新出来,后续LiveData更新如何判断
 * 2. 这个livedata保存的是某个公众号的历史数据,当切换另一个公众号,则使用内存-数据库等缓存数据
 */
public class OfficialAccountViewModel extends ViewModel {

    private LiveData<List<OAChapter>> chapterLiveData;
    private MutableLiveData<List<WanArticle>> articleLiveData = new MutableLiveData<>();

    public OfficialAccountViewModel() {
        chapterLiveData = DataRepository.getInstance().getChapterData();
    }

    public LiveData<List<OAChapter>> getChapterLiveData() {
        return chapterLiveData;
    }

    public LiveData<List<WanArticle>> getArticleLiveData() {
        return articleLiveData;
    }

    public void updateWanArticleIfNeed(int id, int page) {
        DataRepository.getInstance().updateWanArticleData(articleLiveData, id, page);
    }
}
