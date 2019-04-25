package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.GetActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceRequestEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.CatalogueInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACatalogueLevel;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 04/05/15.
 */

public class CataloguePresenterImpl extends BasePresenter implements CataloguePresenter {

    @Inject
    APIValue apiValue;

    @Inject
    EventBus bus;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private ResourceView<CatalogueLevel> catalogueView;
    private CatalogueInteractor catalogueInteractor;

    public CataloguePresenterImpl(Context context, CatalogueInteractor catalogueInteractor) {
        super(context);
        this.context = context;
        this.catalogueInteractor = catalogueInteractor;
    }

    @Override
    public void getCatalogFromCategory(final String catalogLevel, final String label) {

        bus.post(new ResourceRequestEvent<CatalogueLevel>(EResourceType.CATALOG_LEVEL));
        catalogueInteractor.getCatalogueLevel(catalogLevel,
                new AbstractResourceRequestCallback<PR_ACatalogueLevel>() {

                    @Override
                    public void onSuccess(final PR_ACatalogueLevel pr_aCatalogueLevel) {
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                ResourceResponseEvent responseEvent = new ResourceResponseEvent<>((CatalogueLevel)pr_aCatalogueLevel,
                                        null,
                                        EResourceType.CATALOG_LEVEL);
                                catalogueView.onResourceViewResponse(responseEvent);
                                actionEventHelper.log(new GetActionEventData()
                                        .setResource(EResource.CATALOG)
                                        .setStatus(EActionStatus.getActionStatus(true))
                                        .setValue(catalogLevel));
                            }
                        });

                    }

                    @Override
                    public void onError(final BaseException e) {

                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                catalogueView.onResourceViewResponse(new ResourceResponseEvent<CatalogueLevel>(null, new ResourceException(e), EResourceType.CATALOG_LEVEL));
                                actionEventHelper.log(new GetActionEventData()
                                        .setResource(EResource.CATALOG)
                                        .setStatus(EActionStatus.getActionStatus(false))
                                        .setValue(label));
                            }
                        });

                     }
                });
    }



    @Override
    public void setView(ResourceView<CatalogueLevel> view) {
        catalogueView = view;
    }

}
