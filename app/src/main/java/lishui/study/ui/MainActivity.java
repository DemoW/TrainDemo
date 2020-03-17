package lishui.study.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import lishui.study.R;
import lishui.study.databinding.ActivityMainBinding;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 将NavigationUI和BottomNavigationView相互绑定, 注意fragment id要和menu item id一致
        NavController navController = Navigation.findNavController(
                this, R.id.nav_train_host_fragment);
        NavigationUI.setupWithNavController(mBinding.bottomNavView, navController);
    }

}
