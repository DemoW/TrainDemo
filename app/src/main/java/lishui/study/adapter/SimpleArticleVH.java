package lishui.study.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import lishui.study.bean.WanArticle;
import lishui.study.databinding.ItemRecyclerArticleViewBinding;
import lishui.study.util.Utils;

/**
 * Created by lishui.lin on 19-11-15
 */
public class SimpleArticleVH extends RecyclerView.ViewHolder {

    private ItemRecyclerArticleViewBinding mItemArticleBinding;
    public SimpleArticleVH(@NonNull View itemView) {
        super(itemView);
        mItemArticleBinding = DataBindingUtil.bind(itemView);
        itemView.setOnClickListener(v -> {
            WanArticle wanArticle = (WanArticle) itemView.getTag();
            Utils.startWebViewBrowser(itemView.getContext(), wanArticle.getLink());
        });
    }

    public void updateItemView(WanArticle wanArticle) {
        itemView.setTag(wanArticle);
        mItemArticleBinding.articleTitle.setText(wanArticle.getTitle());
        mItemArticleBinding.publishTime.setText(wanArticle.getNiceDate());
        mItemArticleBinding.superChapterName.setText(wanArticle.getSuperChapterName());
        mItemArticleBinding.authorName.setText(wanArticle.getAuthor());
    }
}
