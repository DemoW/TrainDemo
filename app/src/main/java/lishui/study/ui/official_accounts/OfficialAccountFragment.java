package lishui.study.ui.official_accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lishui.study.R;
import lishui.study.adapter.SimpleArticleAdapter;
import lishui.study.adapter.SimplePagerAdapter;
import lishui.study.adapter.TabLayoutListenerAdapter;
import lishui.study.bean.OAChapter;
import lishui.study.util.Utils;
import lishui.study.viewmodel.OfficialAccountViewModel;

public class OfficialAccountFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.official_account_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.nested_view_pager2)
    ViewPager2 mViewPager2;
    @BindView(R.id.content_loading_bar)
    ContentLoadingProgressBar loadingProgressBar;

    private OfficialAccountViewModel mViewModel;
    private SimplePagerAdapter mPagerAdapter;

    public static OfficialAccountFragment newInstance() {
        return new OfficialAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.official_account_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initTabAndPager2();
        return rootView;
    }

    private void initTabAndPager2() {
        mViewPager2.registerOnPageChangeCallback(pagerCallback);
        mPagerAdapter = new SimplePagerAdapter();
        mViewPager2.setAdapter(mPagerAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayoutListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                // todo:test live data
                OAChapter chapter = (OAChapter) tab.getTag();
                if (chapter != null) {
                    mViewModel.updateWanArticleIfNeed(chapter.getId(), 0);
                }

                mViewPager2.setCurrentItem(tab.getPosition(), true);

                loadingProgressBar.show();
            }
        });

    }

    private ViewPager2.OnPageChangeCallback pagerCallback
            = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            TabLayout.Tab tab = mTabLayout.getTabAt(position);
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

        loadingProgressBar.show();
    }

    private void subscribeToModel(OfficialAccountViewModel viewModel) {
        viewModel.getChapterLiveData().observe(this, oaChapters -> {
            if (oaChapters != null) {
                mTabLayout.removeAllTabs();
                for (OAChapter oaChapter : oaChapters) {
                    TabLayout.Tab tab = mTabLayout.newTab();
                    tab.setText(oaChapter.getName());
                    tab.setTag(oaChapter);
                    mTabLayout.addTab(tab);
                }

                if (mTabLayout.getTabCount() > Utils.MIN_TAB_COUNT_THRESHOLD) {
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                } else {
                    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                }

                // update view pager
                List<SimpleArticleAdapter> simpleArticleAdapterList = new ArrayList<>();
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    SimpleArticleAdapter simpleArticleAdapter = new SimpleArticleAdapter();
                    simpleArticleAdapterList.add(simpleArticleAdapter);
                }
                mPagerAdapter.updatePagerAdapter(simpleArticleAdapterList);
            }
        });

        viewModel.getArticleLiveData().observe(this, wanArticles -> {
            if (wanArticles != null) {
                mPagerAdapter.updateArticleAdapter(wanArticles, mTabLayout.getSelectedTabPosition());
            }
            loadingProgressBar.hide();
            // LogUtil.d("wanArticles: " + (wanArticles == null ? 0 : wanArticles.size()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager2.unregisterOnPageChangeCallback(pagerCallback);
        unbinder.unbind();
    }
}
