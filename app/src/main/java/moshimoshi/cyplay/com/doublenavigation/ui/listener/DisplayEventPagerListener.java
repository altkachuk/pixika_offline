package moshimoshi.cyplay.com.doublenavigation.ui.listener;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.DisplayEventFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ViewPagerAdapter;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

/**
 * Created by romainlebouc on 12/06/2017.
 */

public class DisplayEventPagerListener implements ViewPager.OnPageChangeListener {

    private final ViewPagerAdapter viewPagerAdapter;
    private int currentPage;

    public DisplayEventPagerListener(ViewPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            if (viewPagerAdapter != null) {
                Fragment fragment = viewPagerAdapter.getItem(currentPage);
                if (fragment instanceof DisplayEventFragment) {
                    ((DisplayEventFragment) fragment).logEvent();
                }
            }
        }

    }
}
