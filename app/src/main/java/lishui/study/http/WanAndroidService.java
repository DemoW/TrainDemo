package lishui.study.http;

import java.util.List;

import lishui.study.bean.ArticleResult;
import lishui.study.bean.BannerInfo;
import lishui.study.bean.SquareInfo;
import lishui.study.bean.WanResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lishui.lin on 19-11-12
 */
public interface WanAndroidService {

    /**
     * @return 返回首页轮播图
     */
    @GET("banner/json")
    Call<WanResult<List<BannerInfo>>> listBanner();

    /**
     * https://wanandroid.com/user_article/list/页码/json
     * 页码拼接在url上从0开始
     * @return 根据页码返回相应的广场数据页
     */
    @GET("user_article/list/{page}/json")
    Call<WanResult<ArticleResult<List<SquareInfo>>>> listSquareData(@Path("page") int page);

}
