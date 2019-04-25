package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.view.ShopStatisticsView;
import ninja.cyplay.com.apilibrary.domain.interactor.ShopStatisticsInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.business.PR_Chart;

/**
 * Created by damien on 30/05/16.
 */
public class ShopStatisticsPresenterImpl extends BasePresenter implements ShopStatisticsPresenter {

    private Context context;
    private ShopStatisticsView shopStatisticsView;
    private ShopStatisticsInteractor shopStatisticsInteractor;

    public ShopStatisticsPresenterImpl(Context context, ShopStatisticsInteractor shopStatisticsInteractor) {
        super(context);
        this.context = context;
        this.shopStatisticsInteractor = shopStatisticsInteractor;
    }

    @Override
    public void setView(ShopStatisticsView view) {
        this.shopStatisticsView = view;
    }

    @Override
    public void getShopStats() {
        shopStatisticsView.showLoading();
        shopStatisticsInteractor.getStats(new ShopStatisticsInteractor.GetStatsCallback() {

            @Override
            public void onSuccess(List<PR_Chart> charts) {
                shopStatisticsView.onStatsSuccess(charts);
            }

            @Override
            public void onError(BaseException e) {
                shopStatisticsView.onError(e.getType(), e.getResponse().getStatus(), e.getResponse().getDetail());
            }

        });
    }

}
