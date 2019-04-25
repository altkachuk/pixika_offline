package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sale;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractPaginatedResourceRequestCallbackImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.view.ChangeSaleView;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;
import ninja.cyplay.com.apilibrary.domain.interactor.BasketInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SaleInteractor;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasket;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketItem;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_Sale;

/**
 * Created by andre on 28-Feb-19.
 */

public class SalesPresneterImpl extends BasePresenter implements SalesPresenter {

    @Inject
    CustomerContext customerContext;

    SaleInteractor saleInteractor;
    BasketInteractor basketInteractor;
    private ResourceView<List<Sale>> saleView;
    private ChangeSaleView changeSaleView;

    public SalesPresneterImpl(Context context, SaleInteractor saleInteractor, BasketInteractor basketInteractor) {
        super(context);
        this.saleInteractor = saleInteractor;
        this.basketInteractor = basketInteractor;
    }

    @Override
    public void setView(ResourceView<List<Sale>> view) {
        saleView = view;
    }

    @Override
    public void setRemoveView(ChangeSaleView changeSaleView) {
        this.changeSaleView = changeSaleView;
    }

    @Override
    public void getAllSales() {
        saleInteractor.getAllSales(new AbstractPaginatedResourceRequestCallbackImpl<PR_Sale>() {
            @Override
            public void onSuccess(final List<PR_Sale> pr_sales, Integer count, String previous, String next) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        ResourceResponseEvent responseEvent = new ResourceResponseEvent<List<PR_Sale>>(pr_sales, null, null);
                        saleView.onResourceViewResponse(responseEvent);
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        ;
                    }
                });
            }
        });
    }

    public void removeSale(String id) {
        saleInteractor.removeSale(id, new AbstractResourceRequestCallback<PR_Sale>() {
            @Override
            public void onSuccess(PR_Sale pr_sale) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onRemoveSuccess();
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onRemoveError(new ResourceException(e));
                    }
                });
            }
        });
    }

    public void editSale(final Sale sale) {
        changeSaleView.showLoading();

        customerContext.setCustomerId(sale.getCustomer().getId());
        customerContext.setCustomer(sale.getCustomer());

        clearBasketOfCustomer(customerContext.getCustomerId(), sale);
    }

    private void clearBasketOfCustomer(String customerId, final Sale sale) {
        basketInteractor.clearBasket(customerId, new AbstractResourceRequestCallback<PR_ABasket>() {
            @Override
            public void onSuccess(PR_ABasket pr_aBasket) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        removeSale(sale);
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onAddBasketItemsError(new ResourceException(e));
                    }
                });
            }
        });
    }

    private void removeSale(final Sale sale) {
        saleInteractor.removeSale(sale.getId(), new AbstractResourceRequestCallback<PR_Sale>() {
            @Override
            public void onSuccess(PR_Sale pr_sale) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        addBasketItems(sale.getBasket().getItems());
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onAddBasketItemsError(new ResourceException(e));
                    }
                });
            }
        });
    }

    private void addBasketItems(List<BasketItem> basketItems) {
        basketInteractor.addBasketItemsToCustomerBasket(customerContext.getCustomerId(), basketItems, new AbstractResourceRequestCallback<List<PR_ABasketItem>>() {
            @Override
            public void onSuccess(List<PR_ABasketItem> pr_aBasketItems) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onAddBasketItemsSuccess();
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        changeSaleView.onAddBasketItemsError(new ResourceException(e));
                    }
                });
            }
        });
    }
}
