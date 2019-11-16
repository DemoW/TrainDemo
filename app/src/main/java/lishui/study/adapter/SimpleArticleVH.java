package lishui.study.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.study.R;
import lishui.study.bean.WanArticle;
import lishui.study.util.Utils;

/**
 * Created by lishui.lin on 19-11-15
 */
public class SimpleArticleVH extends RecyclerView.ViewHolder {

    @BindView(R.id.article_title)
    TextView titleTv;
    @BindView(R.id.publish_time)
    TextView publishTimeTv;
    @BindView(R.id.super_chapter_name)
    TextView chapterTv;
    @BindView(R.id.author_name)
    TextView authorTv;

    public SimpleArticleVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            WanArticle wanArticle = (WanArticle) itemView.getTag();
            Utils.startWebViewBrowser(itemView.getContext(), wanArticle.getLink());
        });
    }

    public void updateItemView(WanArticle wanArticle) {
        itemView.setTag(wanArticle);
        titleTv.setText(wanArticle.getTitle());
        publishTimeTv.setText(wanArticle.getNiceDate());
        chapterTv.setText(wanArticle.getSuperChapterName());
        authorTv.setText(wanArticle.getAuthor());
    }
}
