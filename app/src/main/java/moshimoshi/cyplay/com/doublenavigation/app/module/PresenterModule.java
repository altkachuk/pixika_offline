package moshimoshi.cyplay.com.doublenavigation.app.module;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CalendarEventList;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.DashboardEventList;
import moshimoshi.cyplay.com.doublenavigation.presenter.AdyenPosPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.AdyenPosPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.AuthenticationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketOfferPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketOfferPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.BasketPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CalendarPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CalendarPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CataloguePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CataloguePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CreateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CreateCustomerInfoPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerAddressesPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerCompletionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerCompletionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerSearchPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.CustomerAddressesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.DQEAddressPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.DQEAddressPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeedbackPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeedbackPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetConfigPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetConfigPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetCustomerInfoPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeliveryModesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeliveryModesPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeviceConfigurationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetDeviceConfigurationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetEventsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetEventsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetProductsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetSalesHistoryPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.GetShopsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.HomeTweetPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.HomeTweetPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ImportPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ImportPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SynchronizationPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SynchronizationPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.OffersPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentSharePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SalesPresneterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.WishlistPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentTransactionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.PaymentTransactionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductReviewPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductReviewPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSearchPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSearchPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSharePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSuggestionPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProductSuggestionPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ProvisionDevicePresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ScannerPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ScannerPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.SellerEventsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.SellerEventsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.ShopStatisticsPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.ShopStatisticsPresenterImpl;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenter;
import moshimoshi.cyplay.com.doublenavigation.presenter.UpdateCustomerInfoPresenterImpl;
import ninja.cyplay.com.apilibrary.domain.interactor.AddressInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.BasketInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CatalogueInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerOfferInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.DQEAddressCompleteInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.DeliveryModeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.FeeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.FeedbackInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SynchronizationInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.OfferInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentShareInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewAttributeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductShareInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SaleInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ScannerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ShopStatisticsInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.TweetInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.WishlistInteractor;

/**
 * Created by damien on 19/04/16.
 */
@Module
public class PresenterModule {

    public PresenterModule() {
    }

    @Provides
    public GetConfigPresenter provideGetConfigPresenter(Context context, ClientInteractor clientInteractor) {
        return new GetConfigPresenterImpl(context, clientInteractor);
    }

    @Provides
    public GetDeviceConfigurationPresenter provideGetSellersPresenter(Context context, SellerInteractor sellerInteractor) {
        return new GetDeviceConfigurationPresenterImpl(context, sellerInteractor);
    }

    @Provides
    public GetShopsPresenter proviceGetShopsPresenter(Context context, ClientInteractor clientInteractor) {
        return new GetShopsPresenterImpl(context, clientInteractor);
    }

    @Provides
    public ProvisionDevicePresenter provideProvisionDevicePresenter(Context context, ClientInteractor clientInteractor) {
        return new ProvisionDevicePresenterImpl(context, clientInteractor);
    }

    @Provides
    AuthenticationPresenter provideAuthenticationPresenter(Context context, SellerInteractor sellerInteractor) {
        return new AuthenticationPresenterImpl(context, sellerInteractor);
    }

    @Provides
    AdyenPosPresenter provideAdyenPosPresenter(Context context) {
        return new AdyenPosPresenterImpl(context);
    }

    @Provides
    CalendarPresenter provideCalendarPresenter(Context context,
                                               EventInteractor eventInteractor,
                                               CalendarEventList calendarEventList,
                                               EventBus bus) {
        return new CalendarPresenterImpl(context, eventInteractor, calendarEventList, bus);
    }

    @Provides
    SellerEventsPresenter provideSellerEventsPresenter(Context context,
                                                       EventInteractor eventInteractor,
                                                       DashboardEventList dashboardEventList,
                                                       EventBus bus) {
        return new SellerEventsPresenterImpl(context, eventInteractor, dashboardEventList, bus);
    }

    @Provides
    CataloguePresenter cataloguePresenter(Context context, CatalogueInteractor catalogueInteractor) {
        return new CataloguePresenterImpl(context, catalogueInteractor);
    }

    @Provides
    GetProductsPresenter getProductsPresenter(Context context, ProductInteractor productInteractor) {
        return new GetProductsPresenterImpl(context, productInteractor);
    }

    @Provides
    GetProductPresenter getProductPresenter(Context context, ProductInteractor productInteractor) {
        return new GetProductPresenterImpl(context, productInteractor);
    }

    @Provides
    CustomerCompletionPresenter customerCompletionPresenter(Context context, CustomerInteractor customerInteractor) {
        return new CustomerCompletionPresenterImpl(context, customerInteractor);
    }

    @Provides
    CustomerSearchPresenter customerSearchPresenter(Context context, CustomerInteractor customerInteractor) {
        return new CustomerSearchPresenterImpl(context, customerInteractor);
    }

    @Provides
    ProductSearchPresenter productSearchPresenter(Context context, ProductInteractor productInteractor) {
        return new ProductSearchPresenterImpl(context, productInteractor);
    }

    @Provides
    ProductSharePresenter productSharePresenter(Context context, ProductShareInteractor productShareInteractor) {
        return new ProductSharePresenterImpl(context, productShareInteractor);
    }

    @Provides
    ProductSuggestionPresenter productSuggestionPresenter(Context context, ProductInteractor productInteractor) {
        return new ProductSuggestionPresenterImpl(context, productInteractor);
    }

    @Provides
    UpdateCustomerInfoPresenter updateCustomerInfoPresenter(Context context, CustomerInteractor customerInteractor) {
        return new UpdateCustomerInfoPresenterImpl(context, customerInteractor);
    }

    @Provides
    GetCustomerInfoPresenter getCustomerInfoPresenter(Context context, CustomerInteractor customerInteractor) {
        return new GetCustomerInfoPresenterImpl(context, customerInteractor);
    }

    @Provides
    ProductReviewPresenter productReviewPresenter(Context context,
                                                  ProductReviewInteractor productReviewInteractor,
                                                  ProductReviewAttributeInteractor productReviewAttributeInteractor) {
        return new ProductReviewPresenterImpl(context, productReviewInteractor, productReviewAttributeInteractor);
    }


    @Provides
    CreateCustomerInfoPresenter createCustomerInfoPresenter(Context context, CustomerInteractor customerInteractor) {
        return new CreateCustomerInfoPresenterImpl(context, customerInteractor);
    }

    @Provides
    FeePresenter feePresenter(Context context, FeeInteractor feeInteractor) {
        return new FeePresenterImpl(context, feeInteractor);
    }


    @Provides
    GetSalesHistoryPresenter getSalesHistoryPresenter(Context context, CustomerInteractor customerInteractor) {
        return new GetSalesHistoryPresenterImpl(context, customerInteractor);
    }

    @Provides
    OffersPresenter getOffersPresenter(Context context, OfferInteractor offerInteractor) {
        return new OffersPresenterImpl(context, offerInteractor);
    }

    @Provides
    ScannerPresenter scannerPresenter(Context context, ScannerInteractor scannerInteractor) {
        return new ScannerPresenterImpl(context, scannerInteractor);
    }

    @Provides
    WishlistPresenter opinionPresenter(Context context, WishlistInteractor wishlistInteractor) {
        return new WishlistPresenterImpl(context, wishlistInteractor);
    }

    @Provides
    BasketPresenter sellerBasketPresenter(Context context, BasketInteractor sellerInteractor,
                                          ProductInteractor productInteractor
    ) {
        return new BasketPresenterImpl(context, sellerInteractor, productInteractor);
    }

    @Provides
    BasketOfferPresenter basketOfferPresenter(Context context,
                                              CustomerOfferInteractor customerOfferInteractor) {
        return new BasketOfferPresenterImpl(context, customerOfferInteractor);
    }

    @Provides
    CustomerAddressesPresenter customerAddressesPresenter(Context context, AddressInteractor addressInteractor) {
        return new CustomerAddressesPresenterImpl(context, addressInteractor);
    }

    @Provides
    HomeTweetPresenter homeTweetPresenter(Context context, TweetInteractor tweetInteractor) {
        return new HomeTweetPresenterImpl(context, tweetInteractor);
    }

    @Provides
    PaymentPresenter paymentPresenter(Context context, PaymentInteractor paymentInteractor) {
        return new PaymentPresenterImpl(context, paymentInteractor);
    }

    @Provides
    PaymentsPresenter paymentsPresenter(Context context, PaymentInteractor paymentInteractor) {
        return new PaymentsPresenterImpl(context, paymentInteractor);
    }

    @Provides
    PaymentTransactionPresenter paymentTransactionPresenter(Context context, PaymentInteractor paymentInteractor) {
        return new PaymentTransactionPresenterImpl(context, paymentInteractor);
    }


    @Provides
    GetEventsPresenter getEventsPresenter(Context context, EventInteractor eventInteractor) {
        return new GetEventsPresenterImpl(context, eventInteractor);
    }

    @Provides
    ShopStatisticsPresenter shopStatisticsPresenter(Context context, ShopStatisticsInteractor shopStatisticsInteractor) {
        return new ShopStatisticsPresenterImpl(context, shopStatisticsInteractor);
    }


    @Provides
    GetDeliveryModesPresenter getDeliveryModesPresenter(Context context, DeliveryModeInteractor deliveryModeInteractor) {
        return new GetDeliveryModesPresenterImpl(context, deliveryModeInteractor);

    }

    @Provides
    PaymentSharePresenter paymentSharePresenter(Context context, PaymentShareInteractor paymentShareInteractor) {
        return new PaymentSharePresenterImpl(context, paymentShareInteractor);

    }

    @Provides
    DQEAddressPresenter dqeAddressPresenter(Context context, DQEAddressCompleteInteractor dQEAddressCompleteInteractor) {
        return new DQEAddressPresenterImpl(context, dQEAddressCompleteInteractor);

    }

    @Provides
    FeedbackPresenter feedbackPresenter(Context context, FeedbackInteractor feedbackInteractor) {
        return new FeedbackPresenterImpl(context, feedbackInteractor);

    }

    @Provides
    SalesPresenter salesPresenter(Context context, SaleInteractor saleInteractor, BasketInteractor basketInteractor) {
        return new SalesPresneterImpl(context, saleInteractor, basketInteractor);
    }

    @Provides
    ImportPresenter importPresenter(Context context, SynchronizationInteractor importInteractor) {
        return new ImportPresenterImpl(context, importInteractor);
    }

    @Provides
    SynchronizationPresenter synchronizationPresenter(Context context, CustomerInteractor customerInteractor, SaleInteractor saleInteractor, SynchronizationInteractor importInteractor) {
        return new SynchronizationPresenterImpl(context, customerInteractor, saleInteractor, importInteractor);
    }
}
