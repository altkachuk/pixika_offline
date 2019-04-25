package moshimoshi.cyplay.com.doublenavigation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.presenter.ShopStatisticsPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseFragment;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.LoadingView;
import moshimoshi.cyplay.com.doublenavigation.view.ShopStatisticsView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.business.PR_Chart;

/**
 * Created by damien on 08/04/16.
 */
public class SellerDashboardStatsFragment extends BaseFragment implements ShopStatisticsView {

    @Inject
    ShopStatisticsPresenter shopStatisticsPresenter;

    @BindView(R.id.root)
    View rootView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.state_loading_view)
    LoadingView stateLoadingView;

    @BindView(R.id.pager_left_arrow)
    ImageView leftArrow;

    @BindView(R.id.pager_right_arrow)
    ImageView rightArrow;

    private ScreenSlidePagerAdapter adapter;

    private List<StatsFragment> statsFragmentList;

    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_dashboard_stats, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // List fillStocks
        statsFragmentList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDesign();
        // set Presenter's view
        shopStatisticsPresenter.setView(this);
        // id Stats
        shopStatisticsPresenter.getShopStats();
        // show and hide ic_decrease accroding to the pager position
        leftArrow.setVisibility(View.GONE);
        rightArrow.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                leftArrow.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
                rightArrow.setVisibility((adapter.getCount() == position + 1) ? View.GONE : View.VISIBLE);
                // Check if this is the page you want.
            }
        });
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
//        DesignUtils.setBackgroundColor(rootView, FeatureColor.PRIMARY_DARK);
    }

    // -------------------------------------------------------------------------------------------
    //                                      View Impl
    // -------------------------------------------------------------------------------------------

    @Override
    public void showLoading() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADING);
    }

    @Override
    public void onError() {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }

    @Override
    public void onStatsSuccess(List<PR_Chart> charts) {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.LOADED);
        statsFragmentList.clear();
        if (charts != null) {
            // display ic_decrease for next page
            if (charts.size() > 1)
                rightArrow.setVisibility(View.VISIBLE);
            // load the right chart
            for (int i = 0; i < charts.size(); i++) {
                PR_Chart chart = charts.get(i);
                if (chart != null && chart.getType() != null) {
                    switch (chart.getType()) {
                        case "HorizontalBarChart":
                            StatsBarFragment statsBarFragmentH = new StatsBarFragment();
                            statsBarFragmentH.setChart(charts.get(i));
                            statsBarFragmentH.setHorizontalChart(true);
                            statsFragmentList.add(statsBarFragmentH);
                            break;
                        case "VerticalBarChart":
                            StatsBarFragment statsBarFragmentV = new StatsBarFragment();
                            statsBarFragmentV.setChart(charts.get(i));
                            statsBarFragmentV.setHorizontalChart(false);
                            statsFragmentList.add(statsBarFragmentV);
                            break;
                        case "PieChart":
                            PieChartFragment pieChartFragment = new PieChartFragment();
                            pieChartFragment.setChart(charts.get(i));
                            statsFragmentList.add(pieChartFragment);
                            break;
                        case "LineChart":
                            StatsLineChartFragment statsLineChartFragment = new StatsLineChartFragment();
                            statsLineChartFragment.setChart(charts.get(i));
                            statsFragmentList.add(statsLineChartFragment);
                            break;
                        default:
                            StatsFragment statsFragment = new StatsFragment();
                            statsFragment.setChart(charts.get(i));
                            statsFragmentList.add(statsFragment);
                            break;
                    }
                }
            }
            // Create adapter
            adapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
            // Set data
            adapter.setFragments(statsFragmentList);
            // wrap pager to provide a minimum of 4 pages
//            MinFragmentPagerAdapter wrappedMinAdapter = new MinFragmentPagerAdapter(getChildFragmentManager());
//            wrappedMinAdapter.setAdapter(adapter);
            // wrap pager to provide infinite paging with wrap-around
//            PagerAdapter wrappedAdapter = new InfinitePagerAdapter(wrappedMinAdapter);
//            viewPager.setAdapter(wrappedAdapter);
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {
        stateLoadingView.setLoadingState(LoadingView.LoadingState.FAILED);
    }

    // -------------------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------------------

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private List<StatsFragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<StatsFragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments != null ? fragments.size() : 0;
        }

    }

    // -------------------------------------------------------------------------------------------
    //                                      Action(s)
    // -------------------------------------------------------------------------------------------

    @OnClick(R.id.reload_button)
    public void onReloadClick() {
        shopStatisticsPresenter.getShopStats();
    }

}
