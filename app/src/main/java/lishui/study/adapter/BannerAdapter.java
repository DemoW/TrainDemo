package lishui.study.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lishui.study.R;
import lishui.study.bean.BannerInfo;
import lishui.study.common.DataViewHolder;
import lishui.study.databinding.ItemRecyclerBannerViewBinding;

public class BannerAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private final Fragment mFragment;
    private List<BannerInfo> bannerList;

    public BannerAdapter(Fragment fragment, List<BannerInfo> bannerList) {
        this.mFragment = fragment;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemRecyclerBannerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mFragment.getContext()),
                R.layout.item_recycler_banner_view, viewGroup, false);
        return new DataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder viewHolder, int i) {
        ItemRecyclerBannerViewBinding binding = (ItemRecyclerBannerViewBinding) viewHolder.binding;
        BannerInfo banner = bannerList.get(i);
        if (banner != null){
            binding.setBanner(banner);
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

    public boolean isBannerDataLoadContinue() {
        return bannerList != null && bannerList.isEmpty();
    }
}
