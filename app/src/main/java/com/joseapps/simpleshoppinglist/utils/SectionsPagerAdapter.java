package com.joseapps.simpleshoppinglist.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment (Fragment fragment){
        mFragmentList.add(fragment);
    }

}
