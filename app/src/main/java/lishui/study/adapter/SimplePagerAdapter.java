package lishui.study.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lishui.study.R;
import lishui.study.bean.WanArticle;

/**
 * Created by lishui.lin on 19-11-15
 */
public class SimplePagerAdapter extends RecyclerView.Adapter<SimplePagerAdapter.PagerVH> {

    private List<SimpleArticleAdapter> articleAdapterList = new ArrayList<>();

    public SimplePagerAdapter() {}

    public SimplePagerAdapter(List<SimpleArticleAdapter> tempAdapterList) {
        if (tempAdapterList != null) {
            this.articleAdapterList = tempAdapterList;
        }
    }

    @NonNull
    @Override
    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pagerView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.pager_item_simple_layout, parent, false);
        return new PagerVH(pagerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerVH holder, int position) {
        if (articleAdapterList.size() > position) {
            SimpleArticleAdapter simpleArticleAdapter = articleAdapterList.get(position);
            RecyclerView recyclerView = (RecyclerView) holder.itemView.findViewById(R.id.simple_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), RecyclerView.VERTICAL));
            recyclerView.setAdapter(simpleArticleAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return articleAdapterList.size();
    }

    public void updatePagerAdapter(List<SimpleArticleAdapter> tempAdapterList) {
        if (tempAdapterList != null) {
            articleAdapterList.clear();
            articleAdapterList.addAll(tempAdapterList);
            notifyDataSetChanged();
        }
    }

    public void updateArticleAdapter(List<WanArticle> wanArticles, int position) {
        if (wanArticles != null && articleAdapterList.size() > position) {
            SimpleArticleAdapter simpleArticleAdapter = articleAdapterList.get(position);
            simpleArticleAdapter.updateAdapter(wanArticles);
        }
    }

    class PagerVH extends RecyclerView.ViewHolder {

        public PagerVH(@NonNull View itemView) {
            super(itemView);
        }
    }

}
