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
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lishui.study.R;
import lishui.study.adapter.BannerAdapter;
import lishui.study.bean.BannerInfo;
import lishui.study.util.NetManager;
import lishui.study.viewmodel.MainViewModel;

public class MainFragment extends Fragment {

    private static final long MIN_REQUEST_INTERVAL = 60_000;

    private MainViewModel mViewModel;
    private Unbinder unbinder;

    @BindView(R.id.loading_bar)
    ContentLoadingProgressBar mLoadingBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private List<BannerInfo> bannerList = new ArrayList<>();
    private BannerAdapter bannerAdapter;
    private boolean lastLoadSuccess;
    private long lastTimeUpdate;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        bannerAdapter = new BannerAdapter(requireContext(), bannerList);
        recyclerView.setAdapter(bannerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        subscribeToModel(mViewModel);
        showLoadingStatus(true);
    }

    private void subscribeToModel(MainViewModel mainViewModel) {
        mainViewModel.getBannerLiveList().observe(this, banners -> {
            if (banners == null || banners.isEmpty()){
                BannerInfo tempBanner = new BannerInfo();
                tempBanner.setTitle("别着急，再等等啦~");
                tempBanner.setUrl("");
                tempBanner.setImagePath("");
                bannerAdapter.updateBannerData(new ArrayList<>(Collections.singletonList(tempBanner)));
                lastLoadSuccess = false;
            } else {
                bannerAdapter.updateBannerData(banners);
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
            mLoadingBar.show();
        } else {
            mLoadingBar.hide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showLoadingStatus(false);
        unbinder.unbind();
    }
}
