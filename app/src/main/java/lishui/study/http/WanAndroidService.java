package lishui.study.http;

import java.util.List;

import lishui.study.bean.WanArticleResult;
import lishui.study.bean.BannerInfo;
import lishui.study.bean.OAChapter;
import lishui.study.bean.WanArticle;
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
    Call<WanResult<WanArticleResult<List<WanArticle>>>> listSquareData(@Path("page") int page);

    /**
     * https://wanandroid.com/wxarticle/chapters/json
     *
     * @return 公众号列表
     */
    @GET("wxarticle/chapters/json")
    Call<WanResult<List<OAChapter>>> listWxChapter();

    /**
     * https://wanandroid.com/wxarticle/list/408/1/json
     *
     * @param id 公众号id
     * @param page 页码
     * @return 某个公众号历史文章数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Call<WanResult<WanArticleResult<List<WanArticle>>>> listWxArticle(@Path("id") int id,@Path("page") int page);
}
