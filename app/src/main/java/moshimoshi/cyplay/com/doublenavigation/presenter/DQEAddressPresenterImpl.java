package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.DQEAddressCompleteInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.DQEResult;

/**
 * Created by wentongwang on 08/06/2017.
 */

public class DQEAddressPresenterImpl extends BasePresenter implements DQEAddressPresenter {

    private ResourceView<List<DQEResult>> dqeView;
    private DQEAddressCompleteInteractor dqeAddressCompleteInteractor;

    private String licence;

    public DQEAddressPresenterImpl(Context context, DQEAddressCompleteInteractor dqeAddressCompleteInteractor) {
        super(context);
        this.dqeAddressCompleteInteractor = dqeAddressCompleteInteractor;

    }

    @Override
    public void setBaseUrl(String baseUrl) {
        dqeAddressCompleteInteractor.setBaseUrl(baseUrl);
    }

    @Override
    public void setLicence(String licence){
        this.licence = licence;
    }

    @Override
    public void setView(ResourceView<List<DQEResult>> view) {
        dqeView = view;
    }

    @Override
    public void getAddress(String query, String countryCode) {
        Map<String, String> params = new HashMap<>();
        params.put("Adresse", query);
        params.put("Pays", countryCode);
        params.put("Licence", licence);

        dqeAddressCompleteInteractor.getAddress(params, new AbstractResourceRequestCallback<List<DQEResult>>() {
            @Override
            public void onSuccess(List<DQEResult> dqeResults) {
                ResourceResponseEvent<List<DQEResult>> resourceResourceResponseEvent = new ResourceResponseEvent<>(
                        dqeResults,
                        null,
                        null
                );
                dqeView.onResourceViewResponse(resourceResourceResponseEvent);
            }

            @Override
            public void onError(BaseException e) {
                ResourceResponseEvent<List<DQEResult>> resourceResourceResponseEvent = new ResourceResponseEvent<>(
                        null,
                        new ResourceException(e),
                        null
                );
                dqeView.onResourceViewResponse(resourceResourceResponseEvent);
            }
        });
    }
}
