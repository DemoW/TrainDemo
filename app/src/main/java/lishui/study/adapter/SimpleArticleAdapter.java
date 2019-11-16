package lishui.study.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lishui.study.R;
import lishui.study.bean.WanArticle;

/**
 * Created by lishui.lin on 19-11-15
 */
public class SimpleArticleAdapter extends RecyclerView.Adapter<SimpleArticleVH> {

    private List<WanArticle> wanArticleList = new ArrayList<>();

    public SimpleArticleAdapter(List<WanArticle> wanArticleList) {
        this.wanArticleList.addAll(wanArticleList);
    }

    public SimpleArticleAdapter() {}

    @NonNull
    @Override
    public SimpleArticleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_article_view, parent, false);

        return new SimpleArticleVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleArticleVH holder, int position) {
        WanArticle wanArticle = wanArticleList.get(position);
        holder.updateItemView(wanArticle);
    }

    public void updateAdapter(List<WanArticle> wanArticles) {
        if (wanArticles != null) {
            wanArticleList.clear();
            wanArticleList.addAll(wanArticles);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return wanArticleList.size();
    }
}
