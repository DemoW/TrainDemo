package lishui.study.ui;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.demo.blur.view.BlurLayout;
import lishui.study.R;
import lishui.study.common.util.Utilities;

public class MainActivity extends FragmentActivity {


    private List<Integer> mTabFragmentList = new ArrayList<>();
    {
        mTabFragmentList.add(0, R.string.main_fragment_class);
        mTabFragmentList.add(1, R.string.square_fragment_class);
        mTabFragmentList.add(2, R.string.official_account_fragment_class);
    }

    @BindView(R.id.main_view_pager)
    ViewPager2 mViewPager2;
    @BindView(R.id.bottom_nav_view)
    BottomNavigationView mBottomNaviView;
    @BindView(R.id.blur_layout)
    BlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initNaiViewAndPager2();
    }

    private void initNaiViewAndPager2() {
        mViewPager2.setUserInputEnabled(false);
        mViewPager2.setAdapter(pagerAdapter);
        mViewPager2.registerOnPageChangeCallback(pagerCallback);

        mBottomNaviView.setOnNavigationItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager2.setCurrentItem(0);
                    return true;
                case R.id.navigation_square:
                    mViewPager2.setCurrentItem(1);
                    return true;
                case R.id.navigation_official_account:
                    mViewPager2.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }

    private FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return Utilities.getOverrideObject(
                    MainFragment.class, MainActivity.this, mTabFragmentList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTabFragmentList.size();
        }
    };

    private ViewPager2.OnPageChangeCallback pagerCallback =
            new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mBottomNaviView.setSelectedItemId(getNaviMenuId(position));
        }
    };

    private @IdRes int getNaviMenuId(int position) {
        int menuId = R.id.navigation_home;
        switch (position) {
            case 0:
                menuId = R.id.navigation_home;
                break;
            case 1:
                menuId = R.id.navigation_square;
                break;
            case 2:
                menuId = R.id.navigation_official_account;
                break;
            default:
                break;
        }

        return menuId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager2.unregisterOnPageChangeCallback(pagerCallback);
    }
}
