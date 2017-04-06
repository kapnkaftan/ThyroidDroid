package com.unmcelearning.android.thyroidpathology;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by adamk_000 on 8/6/2016.
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Leaderboard", "Learn", "Cases"};

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new LearnMenuFragment();
            case 2:
                return new ReviewMenuFragment();
            default:
                return null;
        }
        /**if (position == 0) {
            return new HomeFragment();
        } else if (position == 2) {
            return new ReviewMenuFragment();
        } else {
            return new LearnMenuFragment();
        }**/
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}