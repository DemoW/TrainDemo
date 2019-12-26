package lishui.study.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.study.R;
import lishui.study.bean.BannerInfo;
import lishui.study.common.image.ImageLoader;
import lishui.study.util.Utils;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final Context mContext;
    private final ImageLoader mImageLoader;
    private List<BannerInfo> bannerList;

    public BannerAdapter(Context context, List<BannerInfo> bannerList) {
        this.mContext = context;
        this.bannerList = bannerList;

        mImageLoader = ImageLoader.build(context);
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
            bannerViewHolder.textView.setText(banner.getTitle());

            if (banner.getImagePath().isEmpty()){
                // BlurManager.with(mContext).blur(mContext.getResources(), R.drawable.alcatel_5v,10f, bannerViewHolder.imageView);
                Glide.with(mContext).load(R.drawable.ic_qiaoba_fly).into(bannerViewHolder.imageView);
            }else {
                Glide.with(mContext).load(banner.getImagePath()).into(bannerViewHolder.imageView);
                // mImageLoader.bindBitmap(banner.getUrl(), bannerViewHolder.imageView);
            }

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

        @BindView(R.id.banner_image)
        ImageView imageView;
        @BindView(R.id.banner_name)
        TextView textView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
