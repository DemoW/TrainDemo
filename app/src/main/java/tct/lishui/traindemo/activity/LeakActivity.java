package tct.lishui.traindemo.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import tct.lishui.traindemo.R;

public class LeakActivity extends AppCompatActivity {

	private static final String TAG = "PT/LeakActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leak);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setTitle("LeakActivity");
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
}
