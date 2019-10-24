package com.wistron.fragmetsinfragment;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    //private int numoftabs;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        //this.numoftabs = numOftabs;
    }

    public void addFragment(Fragment f, String title){
        fragmentList.add(f);
        titleList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
        /*switch(position){
            case 0:
                Log.d("Alex", "pos tab1" );
                return new tab1();
            case 1:
                Log.d("Alex", "pos tab2" );
                return new tab2();
            case 2:
                Log.d("Alex", "pos tab3" );
                return new tab3();
            default:
                return null;
        }*/
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

   // @Override
  //  public int getItemPosition(@NonNull Object object) {
  //      return POSITION_NONE;
  //  }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
