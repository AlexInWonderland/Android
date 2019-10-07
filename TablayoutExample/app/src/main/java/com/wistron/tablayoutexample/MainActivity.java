package com.wistron.tablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabItem tab1, tab2, tab3;
    public PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        tab3 = (TabItem) findViewById(R.id.tab3);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mPagerAdapter = new PageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0)
                    mPagerAdapter.notifyDataSetChanged();
                else if (tab.getPosition() == 1)
                    mPagerAdapter.notifyDataSetChanged();
                else if(tab.getPosition() == 2)
                    mPagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }
}
