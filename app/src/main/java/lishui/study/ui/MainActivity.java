package lishui.study.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lishui.demo.blur.view.BlurLayout;
import lishui.study.R;

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
