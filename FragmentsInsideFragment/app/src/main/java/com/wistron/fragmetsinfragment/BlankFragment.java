package com.wistron.fragmetsinfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private TabLayout tab_layout;
    private ViewPager viewpager;
    //private TabItem tab1, tab2, tab3;

    public MyPagerAdapter pageAdapter;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        //ab1 = view.findViewById(R.id.tab1);
        //tab2 = view.findViewById(R.id.tab2);
        //tab3 = view.findViewById(R.id.tab3);
        viewpager = view.findViewById(R.id.viewpager);
        tab_layout = view.findViewById(R.id.tab_layout);
       /* pageAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), tab_layout.getTabCount());
        Log.d("Alex", String.valueOf(tab_layout.getTabCount()));
        viewpager.setAdapter(pageAdapter);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 1)
                    pageAdapter.notifyDataSetChanged();
                else if(tab.getPosition() == 2)
                    pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        //viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewpager);
        tab_layout.setupWithViewPager(viewpager);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void setUpViewPager(ViewPager v){
        pageAdapter = new MyPagerAdapter(getChildFragmentManager());
        pageAdapter.addFragment(new tab1(), "tab1");
        pageAdapter.addFragment(new tab2(), "tab2");
        pageAdapter.addFragment(new tab3(), "tab3");
        viewpager.setAdapter(pageAdapter);
    }
}
