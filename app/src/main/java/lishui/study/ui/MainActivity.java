package lishui.study.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lishui.study.R;
import lishui.study.common.BaseActivity;
import lishui.study.common.log.LogUtils;
import lishui.study.common.permission.PermissionChecker;
import lishui.study.viewmodel.MainSharedViewModel;

public class MainActivity extends BaseActivity {

    private MainSharedViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConfigs();
        initView();

        new Handler().postDelayed(this::checkPermissions, 1500);
    }

    private void initConfigs() {
        mViewModel = new ViewModelProvider(this).get(MainSharedViewModel.class);
    }
    private void initView() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        initToolBar(drawer, toolbar);

        // 将NavigationUI和BottomNavigationView相互绑定, 注意fragment id要和menu item id一致
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bindToolBar(navController, toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initToolBar(DrawerLayout drawerLayout, Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void bindToolBar(NavController navController, Toolbar toolbar) {
        toolbar.setTitle(R.string.menu_title_home);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                toolbar.setTitle(destination.getLabel());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        if (R.id.search_action == id) {
            startActivitySafely(SearchActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_CODE = 1000;
    private static final String[] sPermission = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void checkPermissions() {
        PermissionChecker permissionManager = new PermissionChecker();
        permissionManager.checkPermissions(this, sPermission, new PermissionChecker.CheckResultCallback() {
            @Override
            public void onPermissionsGranted() {
                LogUtils.d("checkPermissions # permissionRequestList is empty and we have its permission.");
            }
            @Override
            public void onPermissionsDenied(String[] deniedPermission) {
                LogUtils.d("checkPermissions # permissionRequestList=" + Arrays.toString(deniedPermission));
                ActivityCompat.requestPermissions(MainActivity.this, deniedPermission, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            List<String> permissionDenyList = new ArrayList<>();
            List<String> permissionDenyNotAskList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        permissionDenyList.add(permission);
                    } else {
                        permissionDenyNotAskList.add(permission);
                    }
                }
            }

            if (permissionDenyList.isEmpty() && permissionDenyNotAskList.isEmpty()) {
                // has permissions
                LogUtils.d("onRequestPermissionsResult # we have permission to do.");
            } else {
                if (!permissionDenyList.isEmpty()) {
                    LogUtils.d("onRequestPermissionsResult # permissionDenyList=" + permissionDenyList.toString());
                    String[] permissionArray = new String[permissionDenyList.size()];
                    ActivityCompat.requestPermissions(this, permissionDenyList.toArray(permissionArray), REQUEST_CODE);
                }

                if (!permissionDenyNotAskList.isEmpty()) {
                    LogUtils.d("onRequestPermissionsResult # permissionDenyNotAskList=" + permissionDenyNotAskList.toString());
                }
            }
        }
    }
}
