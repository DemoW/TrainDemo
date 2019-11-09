package lishui.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import lishui.study.bean.Banner;
import lishui.study.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final Context mContext;
    private List<Banner> bannerList;

    public BannerAdapter(Context context, List<Banner> bannerList) {
        this.mContext = context;
        this.bannerList = bannerList;
    }

    public void clearBanner(){
        this.bannerList.clear();
    }
    public void setDataSet(List<Banner> banners){
        this.bannerList = banners;
    }
    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_banner_view, viewGroup, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder bannerViewHolder, int i) {
        Banner banner = bannerList.get(i);
        if (banner != null){
            bannerViewHolder.textView.setText(banner.getTitle());
            if (banner.getImagePath().isEmpty()){
                Glide.with(mContext).load(R.drawable.ic_main_load).into(bannerViewHolder.imageView);
            }else {
                Glide.with(mContext).load(banner.getImagePath()).into(bannerViewHolder.imageView);
            }
            bannerViewHolder.itemView.setTag(banner.getUrl());
            bannerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String urlStr = (String) view.getTag();
                    if (!urlStr.isEmpty()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(urlStr);
                        intent.setData(uri);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "等待数据刷新中...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView textView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = cardView.findViewById(R.id.banner_image);
            textView = cardView.findViewById(R.id.banner_name);
        }
    }
}
