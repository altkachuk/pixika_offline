package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.business.PR_Chart;

/**
 * Created by damien on 30/05/16.
 */
public interface ShopStatisticsView extends BaseView {

    void showLoading();

    void onError();

    void onStatsSuccess(List<PR_Chart> charts);

}
