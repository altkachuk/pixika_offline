package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.AddressInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AAddress;


/**
 * Created by romainlebouc on 08/12/2016.
 */

public class CustomerAddressesPresenterImpl extends BasePresenter implements CustomerAddressesPresenter {

    private AddressInteractor addressInteractor;
    private ResourceView<Address> view;

    public CustomerAddressesPresenterImpl(Context context, AddressInteractor addressInteractor) {
        super(context);
        this.addressInteractor = addressInteractor;
    }

    @Override
    public void setView(ResourceView<Address> view) {
        this.view = view;

    }

    @Override
    public void createAddress(String customerId, Address address) {

        addressInteractor.addSubResource(customerId, address, new AbstractResourceRequestCallback<PR_AAddress>() {
            @Override
            public void onSuccess(final PR_AAddress shippingAddress) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        ResourceResponseEvent<Address> resourceResourceResponseEvent = new ResourceResponseEvent<>((Address) shippingAddress, null, EResourceType.ADDRESSES);
                        view.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                });


            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        ResourceResponseEvent<Address> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.ADDRESSES);
                        view.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                });

            }
        });
    }

    @Override
    public void updateAddress(String customerId, Address address) {
        addressInteractor.updateSubResource(customerId, address, new ResourceRequestCallback<PR_AAddress>() {
            @Override
            public void onSuccess(PR_AAddress shippingAddress) {
                ResourceResponseEvent<Address> resourceResourceResponseEvent = new ResourceResponseEvent<>((Address) shippingAddress, null, EResourceType.ADDRESSES);
                view.onResourceViewResponse(resourceResourceResponseEvent);
            }

            @Override
            public void onError(BaseException e) {
                ResourceResponseEvent<Address> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.ADDRESSES);
                view.onResourceViewResponse(resourceResourceResponseEvent);
            }
        });
    }

}
