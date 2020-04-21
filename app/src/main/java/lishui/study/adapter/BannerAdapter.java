package lishui.study.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lishui.study.R;
import lishui.study.bean.BannerInfo;
import lishui.study.common.image.ImageLoader;
import lishui.study.databinding.ItemRecyclerBannerViewBinding;
import lishui.study.util.Utils;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final Context mContext;
    private List<BannerInfo> bannerList;

    public BannerAdapter(Context context, List<BannerInfo> bannerList) {
        this.mContext = context;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_banner_view, viewGroup, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder bannerViewHolder, int i) {
        BannerInfo banner = bannerList.get(i);
        if (banner != null){

            Glide.with(mContext)
                    .load(banner.getImagePath())
                    .placeholder(R.color.base_bg_banner)
                    .into(bannerViewHolder.mBannerItemBinding.bannerImage);

            bannerViewHolder.itemView.setTag(banner.getUrl());
            bannerViewHolder.itemView.setOnClickListener(view -> {
                String urlStr = (String) view.getTag();
                if (!urlStr.isEmpty()) {
                    Utils.startWebViewBrowser(mContext, urlStr);
                } else {
                    Toast.makeText(mContext, "等待数据刷新中...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public void updateBannerData(List<BannerInfo> banners){
        bannerList.clear();
        this.bannerList = banners;
        notifyDataSetChanged();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        ItemRecyclerBannerViewBinding mBannerItemBinding;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            mBannerItemBinding = DataBindingUtil.bind(itemView);
        }
    }
}
