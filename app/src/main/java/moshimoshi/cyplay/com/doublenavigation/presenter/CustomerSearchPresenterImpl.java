package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.SearchActionEventData;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.PaginatedResourceRequestCallbackDefaultImpl;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.RequestCallbackLoger;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomer;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;
import ninja.cyplay.com.apilibrary.models.business.enums.EResource;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by damien on 05/05/15.
 */
public class CustomerSearchPresenterImpl extends BasePresenter implements CustomerSearchPresenter {

    @Inject
    APIValue apiValue;

    @Inject
    ActionEventHelper actionEventHelper;

    private Context context;
    private ResourceView<List<Customer>> customerSearchView;
    private CustomerInteractor customerInteractor;

    public CustomerSearchPresenterImpl(Context context, CustomerInteractor customerInteractor) {
        super(context);
        this.context = context;
        this.customerInteractor = customerInteractor;
    }

    @Override
    public void setView(ResourceView<List<Customer>> view) {
        this.customerSearchView = view;
    }

    @Override
    public void searchCustomer(final String customer) {
        customerInteractor.searchFromName(customer,
                new PaginatedResourceRequestCallbackDefaultImpl<Customer, PR_ACustomer>(context,
                        new RequestCallbackLoger() {
                            @Override
                            public void log(boolean success) {
                                actionEventHelper.log(new SearchActionEventData()
                                        .setResource(EResource.CUSTOMER)
                                        .setValue(customer)
                                        .setStatus(EActionStatus.getActionStatus(success))
                                        .addSearchQueryParams("q", customer));
                            }
                        },
                        customerSearchView,
                        EResourceType.CUSTOMERS));
    }

    @Override
    public void searchCustomer(final String firstName, final String lastName, final String zipCode) {
        customerInteractor.searchFromFirstNameLastNameZipCode(firstName,
                lastName,
                zipCode,
                new PaginatedResourceRequestCallbackDefaultImpl<Customer, PR_ACustomer>(context,
                        new RequestCallbackLoger() {
                            @Override
                            public void log(boolean success) {
                                // Log event
                                actionEventHelper.log(new SearchActionEventData()
                                        .setResource(EResource.CUSTOMER)
                                        .setStatus(EActionStatus.getActionStatus(success))
                                        .addSearchQueryParams("details__first_name", firstName)
                                        .addSearchQueryParams("details__last_name", lastName)
                                        .addSearchQueryParams("details__mail__zipcode", zipCode));
                            }
                        },

                        customerSearchView,
                        EResourceType.CUSTOMERS));

    }

    @Override
    public void searchCustomer(final Map<String, String> filters) {
        customerInteractor.search(filters,
                new PaginatedResourceRequestCallbackDefaultImpl<Customer, PR_ACustomer>(context,
                        new RequestCallbackLoger() {
                            @Override
                            public void log(boolean success) {
                                actionEventHelper.log(new SearchActionEventData()
                                        .setResource(EResource.CUSTOMER)
                                        .setStatus(EActionStatus.getActionStatus(success))
                                        .addSearchQueryParams(filters));
                            }
                        },
                        customerSearchView,
                        EResourceType.CUSTOMERS
                ));

    }


}
