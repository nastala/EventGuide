package com.example.nastala.eventguide;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Nastala on 27.12.2016.
 */

public class SwipeAdapter extends FragmentPagerAdapter {
    private String username, userCity;
    private String[] attributes;

    public SwipeAdapter(FragmentManager fm, String username, String userCity, String[] attributes) {
        super(fm);
        this.username = username;
        this.userCity = userCity;
        this.attributes = attributes;
    }

    public Fragment getItem(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = FilteredEventFragment.newInstance(username, userCity, attributes);
                break;
            case 1:
                fragment = RegisteredEventFragment.newInstance(username);
                break;
        }
        return fragment;
    }



    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    public int getCount(){
        return 2;
    }

    public CharSequence getPageTitle(int position){
        if(position == 0)
            return "Filtered Events";
        else if(position == 1)
            return "Registered Events";
        return null;
    }
}

