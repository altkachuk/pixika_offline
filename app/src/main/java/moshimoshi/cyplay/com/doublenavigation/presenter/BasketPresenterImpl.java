package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketComment;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.EResourceType;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.AddToBasketActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.RemoveFromBasketActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.UpdateBasketItemActionEventData;
import moshimoshi.cyplay.com.doublenavigation.model.business.persistent.ValidateCartActionEvent;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.BasketItemWithStock;
import moshimoshi.cyplay.com.doublenavigation.model.business.utils.ThreeSixtyStocks;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketUpdatedEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.BasketUpdatingEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.FilterResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.AbstractResourceRequestCallback;
import moshimoshi.cyplay.com.doublenavigation.presenter.callback.SafeResourceRequestCallbackExecutor;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.view.BasketView;
import moshimoshi.cyplay.com.doublenavigation.view.RemoveBasketItemsView;
import ninja.cyplay.com.apilibrary.domain.interactor.BasketInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasket;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ABasketItem;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProduct;
import ninja.cyplay.com.apilibrary.models.business.enums.EActionStatus;

/**
 * Created by damien on 27/05/15.
 */
public class BasketPresenterImpl extends BasePresenter implements BasketPresenter {

    @Inject
    ActionEventHelper actionEventHelper;

    @Inject
    ConfigHelper configHelper;

    @Inject
    EventBus bus;

    @Inject
    SellerContext sellerContext;

    @Inject
    CustomerContext customerContext;

    private BasketView basketView;
    private RemoveBasketItemsView removeBasketItemsView;
    private BasketInteractor basketInteractor;
    private ProductInteractor productInteractor;

    private List<BasketItem> basketItemsToDelete;

    public BasketPresenterImpl(Context context,
                               BasketInteractor basketInteractor,
                               ProductInteractor productInteractor) {
        super(context);
        this.basketInteractor = basketInteractor;
        this.productInteractor = productInteractor;
    }

    @Override
    public void setView(BasketView view) {
        this.basketView = view;
    }

    public void setRemoveBasketItemsView(RemoveBasketItemsView removeBasketItemsView) {
        this.removeBasketItemsView = removeBasketItemsView;
    }

    // -------------------------------------------------------------------------------------------
    //                                     Get Basket
    // -------------------------------------------------------------------------------------------

    ResourceRequestCallback<PR_ABasket> customerBasketRequestCallback = new AbstractResourceRequestCallback<PR_ABasket>() {
        @Override
        public void onSuccess(final PR_ABasket pr_aBasket) {
            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                @Override
                public void execute() {
                    Basket basket = (Basket) pr_aBasket;
                    customerContext.setBasket(basket);
                    ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(basket, null, EResourceType.BASKET);
                    if (basketView != null) {
                        basketView.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                    bus.post(new BasketUpdatedEvent(basket, BasketUpdatedEvent.EBasketPersonType.CUSTOMER));
                }
            });
        }

        @Override
        public void onError(BaseException e) {
            ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.BASKET);
            if (basketView != null) {
                basketView.onResourceViewResponse(resourceResourceResponseEvent);
            }
        }
    };

    ResourceRequestCallback<PR_ABasket> linkCustomerBasketRequestCallback = new AbstractResourceRequestCallback<PR_ABasket>() {
        @Override
        public void onSuccess(final PR_ABasket pr_aBasket) {
            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                @Override
                public void execute() {
                    Basket basket = (Basket) pr_aBasket;
                    customerContext.setBasket(basket);
                    ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(basket, null, EResourceType.BASKET);
                    if (basketView != null) {
                        basketView.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                    BasketPresenterImpl.this.getSellerBasket();
                    bus.post(new BasketUpdatedEvent(basket, BasketUpdatedEvent.EBasketPersonType.CUSTOMER));
                }
            });
        }

        @Override
        public void onError(BaseException e) {
            ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.BASKET);
            if (basketView != null) {
                basketView.onResourceViewResponse(resourceResourceResponseEvent);
            }
        }
    };


    ResourceRequestCallback<PR_ABasket> sellerBasketRequestCallback = new AbstractResourceRequestCallback<PR_ABasket>() {

        @Override
        public void onError(final BaseException e) {
            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                @Override
                public void execute() {
                    ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(null, new ResourceException(e), EResourceType.BASKET);
                    if (basketView != null) {
                        basketView.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                }
            });
        }

        @Override
        public void onSuccess(final PR_ABasket pr_aBasket) {
            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                @Override
                public void execute() {
                    Basket basket = (Basket) pr_aBasket;
                    sellerContext.setBasket(basket);

                    ResourceResponseEvent<Basket> resourceResourceResponseEvent = new ResourceResponseEvent<>(basket, null, EResourceType.BASKET);
                    if (basketView != null) {
                        basketView.onResourceViewResponse(resourceResourceResponseEvent);
                    }
                    bus.post(new BasketUpdatedEvent(basket, BasketUpdatedEvent.EBasketPersonType.SELLER));
                }
            });
        }
    };

    @Override
    public void getBasket() {
        // if a customer session is active
        if (customerContext != null && customerContext.isCustomerIdentified()) {
            getCustomerCart();
        } else if (sellerContext != null && sellerContext.isSellerIdentified()) {
            getSellerBasket();
        }
    }


    @Override
    public Basket getCachedBasket() {
        if (customerContext != null && customerContext.isCustomerIdentified()) {
            return customerContext.getBasket();
        } else if (sellerContext != null && sellerContext.isSellerIdentified()) {
            return sellerContext.getBasket();
        }
        return null;
    }

    public void getSellerBasket() {
        if (sellerContext != null && sellerContext.getSeller() != null) {
            basketInteractor.getSellerCart(sellerContext.getSeller().getId(), sellerBasketRequestCallback);
        }
    }

    // Will maybe never be used
    public void getCustomerCart() {
        if (customerContext != null && customerContext.isCustomerIdentified()) {
            basketInteractor.getCustomerCart(customerContext.getCustomer().getId(), customerBasketRequestCallback);
        }
    }


    // -------------------------------------------------------------------------------------------
    //                      Manage items in cart
    // -------------------------------------------------------------------------------------------


    public void addUpdateOrDeleteBasketItem(BasketItem basketItem) {
        if (basketItem.getQty() == 0) {
            removeBasketItem(basketItem);
        } else if (getCachedBasket() != null && getCachedBasket().getBasketItem(basketItem.getId()) != null) {
            updateBasketItem(basketItem);
        } else {
            addBasketItem(basketItem);
        }
        bus.post(new BasketUpdatingEvent(basketItem));
    }

    public void addBasketItem(final BasketItem basketItem) {
        if (isCustomerBasket()) {
            addBasketItemToCustomerBasket(basketItem);
        } else {
            addBasketItemToSellerBasket(basketItem);
        }
    }

    public void updateBasketItem(final BasketItem basketItem) {
        if (isCustomerBasket()) {
            updateBasketItemInCustomerBasket(basketItem);
        } else {
            updateBasketItemInSellerBasket(basketItem);
        }
    }

    public void removeBasketItem(final BasketItem basketItem) {
        if (isCustomerBasket()) {
            removeBasketItemFromCustomerBasket(basketItem);
        } else {
            removeBasketItemFromSellerBasket(basketItem);
        }
    }

    @Override
    public void initBasketItemToRemove(List<BasketItem> basketItems) {
        basketItemsToDelete = basketItems;
    }

    @Override
    public void removeBasketItems() {

        List<String> basketItemIds = new ArrayList<>();

        for (BasketItem basketItem : basketItemsToDelete) {
            basketItemIds.add(basketItem.getId());
        }

        if (isCustomerBasket()) {
            removeBasketItemsFromCustomerBasket(basketItemIds);
        } else {
            removeBasketItemsFromSellerBasket(basketItemIds);
        }

    }

    @Override
    public void clearBasket() {
        if (isCustomerBasket()) {
            clearCustomerBasket();
        } else {
            clearSellerBasket();
        }
    }


    @Override
    public void linkSellerBasketToCustomer(String customerId) {
        basketInteractor.linkSellerBasketToCustomer(customerId,
                sellerContext.getSeller().getId(),
                Basket.BASKET_PROJECTION,
                linkCustomerBasketRequestCallback);
    }

    @Override
    public void updateDeliveryFees(Basket basket) {
        basketInteractor.updateBasketDeliveryFees(customerContext.getCustomerId(), basket, new AbstractResourceRequestCallback<PR_ABasket>() {

            @Override
            public void onSuccess(PR_ABasket pr_aBasket) {
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });
    }

    @Override
    public void validateCart(final String customerId, final BasketComment basket) {
        basketInteractor.validateCart(customerId, basket, new AbstractResourceRequestCallback<Void>() {
            @Override
            public void onSuccess(Void v) {
                actionEventHelper.log(new ValidateCartActionEvent().setStatus(EActionStatus.SUCCESS));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        BasketPresenterImpl.this.getBasket();
                        removeBasketItemsView.onRemoveSuccess();
                        customerContext.clearContext();
                        basketView.onValidateCartResponse();
                        //removeBasketItems();
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new ValidateCartActionEvent().setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.PAYMENT));
                    }
                });
            }

        });
    }


    public boolean isCustomerBasket() {
        return customerContext != null && customerContext.isCustomerIdentified() && customerContext.getCustomer() != null;
    }

    public static int callbackCount = 0;

    class BasketItemsStockRequestCallback extends AbstractResourceRequestCallback<PR_AProduct> {

        final AtomicInteger itemCount;
        final BasketItemWithStock basketItemWithStock;

        private int callbackNumber;

        public BasketItemsStockRequestCallback(AtomicInteger itemCount, BasketItemWithStock basketItemWithStock) {
            this.itemCount = itemCount;
            this.basketItemWithStock = basketItemWithStock;
            callbackNumber = callbackCount++;
        }

        @Override
        public void onSuccess(final PR_AProduct pr_aProduct) {

            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {


                @Override
                public void execute() {
                    Product product = (Product) pr_aProduct;
                    ThreeSixtyStocks threeSixtyStocks = new ThreeSixtyStocks(product.getSku(basketItemWithStock.getBasketItem().getSku_id()),
                            configHelper);
                    //TODO : check that stock is OK
                    basketItemWithStock.setThreeSixtyStocks(threeSixtyStocks);
                    int left = itemCount.decrementAndGet();
                    if (left == 0) {
                        basketView.onBasketItemsStocksChecked(true);
                    }
                }
            });
        }

        @Override
        public void onError(final BaseException e) {
            super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                @Override
                public void execute() {
                    itemCount.decrementAndGet();
                    basketView.onBasketItemsStocksChecked(false);
                }
            });
        }

    }

    public void checkBasketStock(List<BasketItemWithStock> basketItemWithStockList) {
        final AtomicInteger itemCount = new AtomicInteger(basketItemWithStockList.size());
        if (basketItemWithStockList != null) {
            for (BasketItemWithStock basketItemWithStock : basketItemWithStockList) {
                BasketItemsStockRequestCallback callback = new BasketItemsStockRequestCallback(itemCount, basketItemWithStock);
                productInteractor.getProductSku(basketItemWithStock.getBasketItem().getProduct_id(),
                        basketItemWithStock.getBasketItem().getSku_id(),
                        Product.PRODUCT_STOCK_PROJECTION,
                        callback);
            }
        }

    }


    public void getBasketItemStock(final BasketItem basketItem) {

        productInteractor.getProductSku(basketItem.getProduct_id(), basketItem.getSku_id(), Product.PRODUCT_STOCK_PROJECTION, new AbstractResourceRequestCallback<PR_AProduct>() {

            @Override
            public void onSuccess(final PR_AProduct product) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onStockResponse(basketItem, new FilterResourceResponseEvent<Product, ProductFilter>((Product) product, null, EResourceType.PRODUCT_STOCK));
                    }
                });
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onStockResponse(basketItem, new FilterResourceResponseEvent<Product, ProductFilter>(null, new ResourceException(e), EResourceType.PRODUCT_STOCK));
                    }
                });
            }
        });

    }
    //=========Customer Basket Operation===========

    private void addBasketItemToCustomerBasket(final BasketItem basketItem) {
        basketInteractor.addBasketItemToCustomerBasket(customerContext.getCustomerId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new AddToBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new AddToBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void updateBasketItemInCustomerBasket(final BasketItem basketItem) {
        basketInteractor.updateBasketItemInCustomerBasket(customerContext.getCustomerId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new UpdateBasketItemActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new UpdateBasketItemActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void removeBasketItemFromCustomerBasket(final BasketItem basketItem) {
        basketInteractor.removeBasketItemFromCustomerBasket(customerContext.getCustomerId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new RemoveFromBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new RemoveFromBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void removeBasketItemsFromCustomerBasket(final List<String> basketItemIds) {
        basketInteractor.removeBasketItemsFromCustomerBasket(customerContext.getCustomerId(), basketItemIds,
                new AbstractResourceRequestCallback<List<PR_ABasketItem>>() {

                    @Override
                    public void onSuccess(List<PR_ABasketItem> pr_aBasketDeleteItems) {
                        actionEventHelper.log(new RemoveFromBasketActionEventData(basketItemIds.toString()).setStatus(EActionStatus.SUCCESS));
                        BasketPresenterImpl.this.getBasket();
                        removeBasketItemsView.onRemoveSuccess();
                    }

                    @Override
                    public void onError(final BaseException e) {
                        actionEventHelper.log(new RemoveFromBasketActionEventData(basketItemIds.toString()).setStatus(EActionStatus.FAILURE));
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                removeBasketItemsView.onRemoveBasketItemsError(new ResourceException(e));
                            }
                        });
                    }
                });

    }


    private void clearCustomerBasket() {
        basketInteractor.clearBasket(customerContext.getCustomerId(), new AbstractResourceRequestCallback<PR_ABasket>() {
            @Override
            public void onSuccess(PR_ABasket pr_aBasket) {
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        removeBasketItemsView.onClearError(new ResourceException(e));
                    }
                });
            }
        });
    }

    //=========Seller Basket Operation===========
    private void addBasketItemToSellerBasket(final BasketItem basketItem) {
        basketInteractor.addBasketItemToSellerBasket(sellerContext.getSeller().getId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new AddToBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new AddToBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void updateBasketItemInSellerBasket(final BasketItem basketItem) {
        basketInteractor.updateBasketItemInSellerBasket(sellerContext.getSeller().getId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new UpdateBasketItemActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new UpdateBasketItemActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void removeBasketItemFromSellerBasket(final BasketItem basketItem) {
        basketInteractor.removeBasketItemFromSellerBasket(sellerContext.getSeller().getId(), basketItem, new AbstractResourceRequestCallback<PR_ABasketItem>() {

            @Override
            public void onSuccess(PR_ABasketItem pr_aBasketItem) {
                actionEventHelper.log(new RemoveFromBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.SUCCESS));
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                actionEventHelper.log(new RemoveFromBasketActionEventData(basketItem.getSku_id()).setStatus(EActionStatus.FAILURE));
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        basketView.onResourceViewResponse(new ResourceResponseEvent<Basket>(null,
                                new ResourceException(e),
                                EResourceType.BASKET));
                    }
                });
            }
        });

    }

    private void removeBasketItemsFromSellerBasket(final List<String> basketItemIds) {
        basketInteractor.removeBasketItemsFromSellerBasket(sellerContext.getSeller().getId(), basketItemIds,
                new AbstractResourceRequestCallback<List<PR_ABasketItem>>() {
                    @Override
                    public void onSuccess(List<PR_ABasketItem> pr_aBasketItems) {
                        actionEventHelper.log(new RemoveFromBasketActionEventData(basketItemIds.toString()).setStatus(EActionStatus.SUCCESS));
                        BasketPresenterImpl.this.getBasket();
                        removeBasketItemsView.onRemoveSuccess();
                    }

                    @Override
                    public void onError(final BaseException e) {
                        actionEventHelper.log(new RemoveFromBasketActionEventData(basketItemIds.toString()).setStatus(EActionStatus.FAILURE));
                        super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                            @Override
                            public void execute() {
                                removeBasketItemsView.onRemoveBasketItemsError(new ResourceException(e));
                            }
                        });
                    }
                });

    }


    private void clearSellerBasket() {
        basketInteractor.clearSellerBasket(sellerContext.getSeller().getId(), new AbstractResourceRequestCallback<PR_ABasket>() {
            @Override
            public void onSuccess(PR_ABasket pr_aBasket) {
                BasketPresenterImpl.this.getBasket();
            }

            @Override
            public void onError(final BaseException e) {
                super.onSafeExecute(new SafeResourceRequestCallbackExecutor() {
                    @Override
                    public void execute() {
                        if (removeBasketItemsView != null) {
                            removeBasketItemsView.onClearError(new ResourceException(e));
                        }
                    }
                });
            }
        });
    }
}
