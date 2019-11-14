package lishui.study.bean;

/**
 * Created by lishui.lin on 19-11-13
 *
 * WanResult<T>的data成员是这个ArticleResult<T>
 */
public class ArticleResult<T> {

    private T datas;

    private int curPage;
    private int size;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ArticleResult{" +
                "datas=" + datas +
                ", curPage=" + curPage +
                ", size=" + size +
                '}';
    }
}
