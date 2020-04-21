package lishui.study.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Collections;

import lishui.demo.base_ui.anim.DepthPageTransformer;
import lishui.demo.base_ui.anim.ZoomOutPageTransformer;
import lishui.study.R;
import lishui.study.adapter.BannerAdapter;
import lishui.study.bean.BannerInfo;
import lishui.study.databinding.FragmentHomeLayoutBinding;
import lishui.study.viewmodel.HomeViewModel;

/**
 * Created by lishui.lin on 20-4-21
 */
public class HomeFragment extends Fragment {

    private FragmentHomeLayoutBinding mBinding;
    private HomeViewModel mViewModel;
    private BannerAdapter mBannerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_layout, container, false);
        initViews();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        subscribeToModel(mViewModel);
        mViewModel.loadBannerDataIfNeed();
    }

    private void initViews() {
        final ViewPager2 pager2 = mBinding.bannerPager;
        pager2.setOffscreenPageLimit(1);
        pager2.setPageTransformer(new ZoomOutPageTransformer());
//        RecyclerView recyclerView = (RecyclerView) pager2.getChildAt(0);
//        if (recyclerView != null) {
//            int padding = getResources().getDimensionPixelOffset(R.dimen.padding_value_16dp);
//            recyclerView.setPadding(padding, 0, padding, 0);
//            recyclerView.setClipToPadding(false);
//        }
        mBannerAdapter = new BannerAdapter(getContext(), new ArrayList<>());
        pager2.setAdapter(mBannerAdapter);
    }

    private void subscribeToModel(HomeViewModel mainViewModel) {
        mainViewModel.getBannerLiveList().observe(getViewLifecycleOwner(), banners -> {
            if (banners == null || banners.isEmpty()){
                BannerInfo tempBanner = new BannerInfo();
                tempBanner.setTitle("别着急，再等等啦~");
                tempBanner.setUrl("");
                tempBanner.setImagePath("");
                mBannerAdapter.updateBannerData(new ArrayList<>(Collections.singletonList(tempBanner)));
            } else {
                mBannerAdapter.updateBannerData(banners);
            }
        });
    }
}
