package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {

    launch launcher;
    public interface launch{
        public void launch();
    }

    public void setLauncher(launch launcher) {
        this.launcher = launcher;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        launcher.launch();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            Thread.sleep(3000);
        }
        catch (Exception e){}
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setElevation(0);
        setContentView(R.layout.activity_launch);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vp_pager);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        TabLayout vp_tab = (TabLayout) findViewById(R.id.vp_tab);
        vp_tab.setupWithViewPager(vpPager);

    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SearchFragment.newInstance(0);
                case 1:
                    return WishlistFragment.newInstance(0);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0)
            return "SEARCH";

            return "WISHLIST";

        }

    }

}
