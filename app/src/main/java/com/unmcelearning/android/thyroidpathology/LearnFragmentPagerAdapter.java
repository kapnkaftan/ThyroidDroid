package com.unmcelearning.android.thyroidpathology;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by adamk_000 on 8/21/2016.
 */
public class LearnFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT_DPC = 3;
    private String tabTitles[] = new String[]{"Read", "Image", "Quiz"};

    public LearnFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 2) {
            return new QuizFragment();
        } else if (position == 1) {
            return new ImageFragment();
        } else {
            return new ReadFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT_DPC;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}