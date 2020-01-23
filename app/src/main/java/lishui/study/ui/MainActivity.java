package lishui.study.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
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

    @BindView(R.id.bottom_nav_view)
    BottomNavigationView mBottomNaviView;
    @BindView(R.id.blur_layout)
    BlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 将NavigationUI和BottomNavigationView相互绑定, 注意fragment id要和menu item id一致
        NavController navController = Navigation.findNavController(
                this, R.id.nav_train_host_fragment);
        NavigationUI.setupWithNavController(mBottomNaviView, navController);
    }

}
