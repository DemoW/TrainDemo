package lishui.study.util;

public class NetConstant {

    public static final String WANANDROID_BASE_URL = "https://www.wanandroid.com/";

    // banner data
    public static final String BANNER_DATA_PATH = "/banner/json";

	// set a flag to decide a normal or slow tct.lishui.traindemo.activity.
	public static final String START_TIME_FLAG = "start_time_flag";

	// taobao ip lib
	public static final String URL_STR = "http://ip.taobao.com/service/getIpInfo.php";

	// banner link
	public static final String BANNER_URL_STR = "https://www.wanandroid.com/banner/json";

	// hot word link
	public static final String HOT_WORD_URL_STR = "https://www.wanandroid.com//hotkey/json";

	// douban movie top250
	/*
	* 豆瓣电影
	* 请求数据{startAnim:int, count:int}
	* 响应数据{startAnim：int, count:int, total:int, title:String, subjects: array}
	* 响应方法：GET
	* */
	public static final String DOUBAN_MOVIE_TOP250 = "https://api.douban.com/v2/movie/top250";

}
