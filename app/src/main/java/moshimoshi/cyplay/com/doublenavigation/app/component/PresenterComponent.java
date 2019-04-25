package moshimoshi.cyplay.com.doublenavigation.app.component;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.PresenterModule;
import moshimoshi.cyplay.com.doublenavigation.presenter.AdyenPosPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketOfferPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CalendarPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CataloguePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CreateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerAddressesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerCompletionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.DQEAddressPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeedbackPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetConfigPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeliveryModesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetEventsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeviceConfigurationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.HomeTweetPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ImportPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SynchronizationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentTransactionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductReviewPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSuggestionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ScannerPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SellerEventsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ShopStatisticsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenter;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;

/**
 * Created by damien on 19/04/16.
 */

@Component(
        modules = {
                PresenterModule.class
        },
        dependencies = {
                ApplicationComponent.class,
                InteractorComponent.class,
                RuntimeComponent.class,
                CalendarComponent.class,
                BusComponent.class,
                OfflineInteractorComponent.class
        }
)
public interface PresenterComponent {

    GetConfigPresenter provideGetConfigPresenter();

    GetDeviceConfigurationPresenter provideGetSellersPresenter();

    GetShopsPresenter proviceGetShopsPresenter();

    ProvisionDevicePresenter provideProvisionDevicePresenter();

    AuthenticationPresenter provideAuthenticationPresenter();

    AdyenPosPresenter provideAdyenPosPresenter();

    CalendarPresenter provideCalendarPresenter();

    SellerEventsPresenter provideSellerEventsPresenter();

    CataloguePresenter cataloguePresenter();

    GetProductsPresenter getProductsPresenter();

    GetProductPresenter getProductPresenter();

    CustomerCompletionPresenter customerCompletionPresenter();

    CustomerSearchPresenter customerSearchPresenter();

    ProductSearchPresenter productSearchPresenter();

    ProductSharePresenter productSharePresenter();

    ProductSuggestionPresenter productSuggestionPresenter();

    UpdateCustomerInfoPresenter updateCustomerInfoPresenter();

    GetCustomerInfoPresenter getCustomerInfoPresenter();

    ProductReviewPresenter productReviewPresenter();

    CreateCustomerInfoPresenter createCustomerInfoPresenter();

    FeePresenter feePresenter();

    GetSalesHistoryPresenter getSalesHistoryPresenter();

    OffersPresenter offersPresenter();

    ScannerPresenter scannerPresenter();

    WishlistPresenter opinionPresenter();

    BasketPresenter sellerBasketPresenter();

    BasketOfferPresenter basketOfferPresenter();

    CustomerAddressesPresenter customerAddressesPresenter();

    HomeTweetPresenter homeTweetPresenter();

    PaymentPresenter paymentPresenter();

    PaymentTransactionPresenter paymentTransactionPresenter();

    GetEventsPresenter getEventsPresenter();

    ShopStatisticsPresenter shopStatisticsPresenter();

    PaymentsPresenter paymentsPresenter();

    GetDeliveryModesPresenter getDeliveryModesPresenter();

    PaymentSharePresenter paymentSharePresenter();

    DQEAddressPresenter dqeAddressPresenter();

    FeedbackPresenter feedbackPresenter();

    SalesPresenter salesPresenter();

    SynchronizationPresenter synchronizationPresenter();

    ImportPresenter importPresenter();
}
