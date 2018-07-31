package tct.lishui.traindemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.bean.Banner;
import tct.lishui.traindemo.util.Constant;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_view, viewGroup, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder bannerViewHolder, int i) {
        Banner banner = bannerList.get(i);
        if (banner != null){
            bannerViewHolder.textView.setText(banner.getTitle());
            Glide.with(mContext).load(banner.getImagePath()).into(bannerViewHolder.imageView);
            bannerViewHolder.itemView.setTag(banner.getUrl());
            bannerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse((String) view.getTag());
                    intent.setData(uri);
                    mContext.startActivity(intent);

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
            imageView = (ImageView)cardView.findViewById(R.id.banner_image);
            textView = (TextView)cardView.findViewById(R.id.banner_name);
        }
    }
}
