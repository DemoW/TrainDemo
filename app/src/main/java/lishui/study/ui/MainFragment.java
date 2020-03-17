package lishui.study.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lishui.study.R;
import lishui.study.adapter.BannerAdapter;
import lishui.study.bean.BannerInfo;
import lishui.study.databinding.MainFragmentBinding;
import lishui.study.util.NetManager;
import lishui.study.viewmodel.MainViewModel;

public class MainFragment extends Fragment {

    private static final long MIN_REQUEST_INTERVAL = 60_000;

    private MainFragmentBinding mBinding;
    private MainViewModel mViewModel;

    private List<BannerInfo> mBannerList = new ArrayList<>();
    private BannerAdapter mBannerAdapter;
    private boolean lastLoadSuccess;
    private long lastTimeUpdate;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = MainFragmentBinding.inflate(inflater, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBannerAdapter = new BannerAdapter(requireContext(), mBannerList);
        mBinding.recyclerView.setAdapter(mBannerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        subscribeToModel(mViewModel);
        showLoadingStatus(true);
    }

    private void subscribeToModel(MainViewModel mainViewModel) {
        mainViewModel.getBannerLiveList().observe(getViewLifecycleOwner(), banners -> {
            if (banners == null || banners.isEmpty()){
                BannerInfo tempBanner = new BannerInfo();
                tempBanner.setTitle("别着急，再等等啦~");
                tempBanner.setUrl("");
                tempBanner.setImagePath("");
                mBannerAdapter.updateBannerData(new ArrayList<>(Collections.singletonList(tempBanner)));
                lastLoadSuccess = false;
            } else {
                mBannerAdapter.updateBannerData(banners);
                lastLoadSuccess = true;
            }
            lastTimeUpdate = System.currentTimeMillis();
            showLoadingStatus(false);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNetworkListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterNetworkListener();
    }

    private void registerNetworkListener(){
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkReceiver, intentFilter);
    }

    private void unregisterNetworkListener(){
        if (networkReceiver != null){
            requireActivity().unregisterReceiver(networkReceiver);
        }
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                if (NetManager.isNetworkConnected(context) && !lastLoadSuccess
                        && System.currentTimeMillis()- lastTimeUpdate > MIN_REQUEST_INTERVAL) {
                    mViewModel.loadBannerDataIfNeed();
                }
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.popup_menu, menu);
    }

    private void showLoadingStatus(boolean isShow) {
        if (isShow) {
            mBinding.loadingBar.show();
        } else {
            mBinding.loadingBar.hide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showLoadingStatus(false);
    }
}
