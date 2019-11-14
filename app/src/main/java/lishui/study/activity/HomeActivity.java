package lishui.study.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.study.R;
import lishui.study.common.util.Utilities;
import lishui.study.fragment.HomeFragment;

public class HomeActivity extends FragmentActivity {

    private static final int MIN_TAB_COUNT_THRESHOLD = 5;

    private List<Integer> mTabFragmentList = new ArrayList<>();
    {
        mTabFragmentList.add(0, R.string.home_fragment_class);
        mTabFragmentList.add(1, R.string.square_fragment_class);
        mTabFragmentList.add(2, R.string.main_fragment_class);
    }


    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.home_view_pager)
    ViewPager2 mViewPager2;
    @BindArray(R.array.train_main_tab_array)
    String[] mTabArray;

    private TabLayoutMediator tabLayoutMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initTabAndPager2();
    }

    private void initTabAndPager2() {
        mViewPager2.setAdapter(pagerAdapter);
        tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            if (position < mTabArray.length) {
                tab.setText(mTabArray[position]);
            }
        });
        tabLayoutMediator.attach();

        if (mTabLayout.getTabCount() > MIN_TAB_COUNT_THRESHOLD) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return Utilities.getOverrideObject(
                    HomeFragment.class, HomeActivity.this, mTabFragmentList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTabFragmentList.size();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabLayoutMediator.detach();
    }
}
