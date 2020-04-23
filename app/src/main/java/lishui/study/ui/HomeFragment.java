package lishui.study.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import lishui.demo.base_ui.anim.ZoomOutPageTransformer;
import lishui.study.R;
import lishui.study.adapter.BannerAdapter;
import lishui.study.common.BaseFragment;
import lishui.study.databinding.FragmentHomeLayoutBinding;
import lishui.study.http.NetworkConstant;
import lishui.study.util.TrainUtils;
import lishui.study.viewmodel.HomeViewModel;
import lishui.study.viewmodel.MainSharedViewModel;

/**
 * Created by lishui.lin on 20-4-21
 */
public class HomeFragment extends BaseFragment {

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
        if (TrainUtils.isNetworkAvailable(requireContext())) {
            mViewModel.loadBannerDataIfNeed();
        } else if (mBannerAdapter.isBannerDataLoadContinue()) {
            Toast.makeText(getContext(), "Network unavailable. Can not load data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        final ViewPager2 pager2 = mBinding.bannerPager;
        pager2.setOffscreenPageLimit(1);
        pager2.setPageTransformer(new ZoomOutPageTransformer());
        mBannerAdapter = new BannerAdapter(this, new ArrayList<>());
        pager2.setAdapter(mBannerAdapter);
    }

    private void subscribeToModel(HomeViewModel viewModel) {
        viewModel.getBannerLiveList().observe(getViewLifecycleOwner(), banners -> {
            if (banners != null && mBannerAdapter.isBannerDataLoadContinue()){
                mBannerAdapter.updateBannerData(banners);
            }
        });

        final MainSharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);
        sharedViewModel.getNetworkLiveState().observe(getViewLifecycleOwner(), state -> {
            if (NetworkConstant.NETWORK_WIFI.equals(state)) {
                if (mBannerAdapter.isBannerDataLoadContinue()) {
                    viewModel.loadBannerDataIfNeed();
                }
            } else if (NetworkConstant.NETWORK_DISABLE.equals(state)) {
                if (mBannerAdapter.isBannerDataLoadContinue()) {
                    Toast.makeText(getContext(), "Network unavailable. Can not load data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
