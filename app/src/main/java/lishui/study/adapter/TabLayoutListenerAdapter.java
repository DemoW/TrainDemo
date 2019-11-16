package lishui.study.adapter;

import com.google.android.material.tabs.TabLayout;

/**
 * Created by lishui.lin on 19-11-15
 */
public abstract class TabLayoutListenerAdapter implements TabLayout.OnTabSelectedListener {

    @Override
    public void onTabSelected(TabLayout.Tab tab){}

    @Override
    public void onTabUnselected(TabLayout.Tab tab){}

    @Override
    public void onTabReselected(TabLayout.Tab tab){}
}
