package lishui.study.ui.official_accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import lishui.study.R;
import lishui.study.adapter.SimpleArticleAdapter;
import lishui.study.adapter.SimplePagerAdapter;
import lishui.study.adapter.TabLayoutListenerAdapter;
import lishui.study.bean.OAChapter;
import lishui.study.databinding.OfficialAccountFragmentBinding;
import lishui.study.util.TrainUtils;
import lishui.study.viewmodel.OfficialAccountViewModel;

public class OfficialAccountFragment extends Fragment {

    private OfficialAccountFragmentBinding mBinding;
    private OfficialAccountViewModel mViewModel;
    private SimplePagerAdapter mPagerAdapter;

    public static OfficialAccountFragment newInstance() {
        return new OfficialAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.official_account_fragment, container, false);
        initTabAndPager2();
        return mBinding.getRoot();
    }

    private void initTabAndPager2() {
        mBinding.nestedViewPager2.registerOnPageChangeCallback(pagerCallback);
        mPagerAdapter = new SimplePagerAdapter();
        mBinding.nestedViewPager2.setAdapter(mPagerAdapter);

        mBinding.officialAccountTabLayout.addOnTabSelectedListener(new TabLayoutListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                // todo:test live data
                OAChapter chapter = (OAChapter) tab.getTag();
                if (chapter != null) {
                    mViewModel.updateWanArticleIfNeed(chapter.getId(), 0);
                }

                mBinding.nestedViewPager2.setCurrentItem(tab.getPosition(), true);
                mBinding.contentLoadingBar.show();
            }
        });
    }

    private ViewPager2.OnPageChangeCallback pagerCallback
            = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            TabLayout.Tab tab = mBinding.officialAccountTabLayout.getTabAt(position);
            if (tab != null) {
                tab.select();
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OfficialAccountViewModel.class);
        subscribeToModel(mViewModel);

        mBinding.contentLoadingBar.show();
    }

    private void subscribeToModel(OfficialAccountViewModel viewModel) {
        viewModel.getChapterLiveData().observe(getViewLifecycleOwner(), oaChapters -> {
            if (oaChapters != null) {
                mBinding.officialAccountTabLayout.removeAllTabs();
                for (OAChapter oaChapter : oaChapters) {
                    TabLayout.Tab tab = mBinding.officialAccountTabLayout.newTab();
                    tab.setText(oaChapter.getName());
                    tab.setTag(oaChapter);
                    mBinding.officialAccountTabLayout.addTab(tab);
                }

                if (mBinding.officialAccountTabLayout.getTabCount() > TrainUtils.MIN_TAB_COUNT_THRESHOLD) {
                    mBinding.officialAccountTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                } else {
                    mBinding.officialAccountTabLayout.setTabMode(TabLayout.MODE_FIXED);
                }

                // update view pager
                List<SimpleArticleAdapter> simpleArticleAdapterList = new ArrayList<>();
                for (int i = 0; i < mBinding.officialAccountTabLayout.getTabCount(); i++) {
                    SimpleArticleAdapter simpleArticleAdapter = new SimpleArticleAdapter();
                    simpleArticleAdapterList.add(simpleArticleAdapter);
                }
                mPagerAdapter.updatePagerAdapter(simpleArticleAdapterList);
            }
        });

        viewModel.getArticleLiveData().observe(getViewLifecycleOwner(), wanArticles -> {
            if (wanArticles != null) {
                mPagerAdapter.updateArticleAdapter(wanArticles, mBinding.officialAccountTabLayout.getSelectedTabPosition());
            }
            mBinding.contentLoadingBar.hide();
            // LogUtil.d("wanArticles: " + (wanArticles == null ? 0 : wanArticles.size()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.nestedViewPager2.unregisterOnPageChangeCallback(pagerCallback);
    }
}
