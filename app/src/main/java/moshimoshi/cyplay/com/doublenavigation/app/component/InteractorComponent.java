package moshimoshi.cyplay.com.doublenavigation.app.component;

import dagger.Component;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.ExecutorComponent;
import ninja.cyplay.com.apilibrary.domain.interactor.AddressInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerOfferInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.DQEAddressCompleteInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.DeliveryModeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.EventInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.FeeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.FeedbackInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.GetQuotationsInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SynchronizationInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.OfferInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.PaymentShareInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewAttributeInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductReviewInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductShareInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.QuotationInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ReportingInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ScannerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ShopStatisticsInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.TweetInteractor;
import ninja.cyplay.com.apilibrary.domain.module.InteractorModule;

/**
 * Created by andre on 30-Aug-18.
 */
@Component(
        modules = {
                InteractorModule.class
        },
        dependencies = {
                ApplicationComponent.class,
                ExecutorComponent.class,
                RepositoryComponent.class
        }
)
public interface InteractorComponent {

    EventInteractor event();
    FeeInteractor feeInteractor();
    OfferInteractor offerInteractor();
    ProductShareInteractor productShareInteractor();
    AddressInteractor addressInteractor();
    ProductReviewInteractor productReviewInteractor();
    ProductReviewAttributeInteractor productReviewAttributeInteractor();
    ScannerInteractor scanner();
    ReportingInteractor reportingInteractor();
    TweetInteractor tweetInteractor();
    PaymentInteractor paymentInteractor();
    ShopStatisticsInteractor shopStatisticsInteractor();
    DeliveryModeInteractor deliveryModeInteractor();
    PaymentShareInteractor paymentShareInteractor();
    CustomerOfferInteractor customerOfferInteractor();
    DQEAddressCompleteInteractor dQEAddressCompleteInteractor();
    FeedbackInteractor suggestionInteractor();
    QuotationInteractor quotationInteractor();
    GetQuotationsInteractor getQuotationsInteractor();
    SynchronizationInteractor importInteractor();
}
