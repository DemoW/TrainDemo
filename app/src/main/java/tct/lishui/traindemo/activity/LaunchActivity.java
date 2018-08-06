package tct.lishui.traindemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.util.Constant;
import tct.lishui.traindemo.view.LauncherView;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "PT/LaunchActivity";

    LauncherView launcherView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        launcherView = findViewById(R.id.launch_view);

        initView();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                launcherView.start();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.start_quick_time) {

            Intent intent = new Intent(this, AppTimeUpActivity.class);
            intent.putExtra(Constant.START_TIME_FLAG, false);
            startActivity(intent);

        } else if (itemId == R.id.slow_start_time) {

            Intent intent = new Intent(this, AppTimeUpActivity.class);
            intent.putExtra(Constant.START_TIME_FLAG, true);
            startActivity(intent);

        } else if (itemId == R.id.launch_heavy_aty) {

            Intent intent = new Intent(this, RenderActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.launch_leak_aty) {

            Intent intent = new Intent(this, LeakActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
