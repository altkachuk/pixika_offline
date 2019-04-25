package moshimoshi.cyplay.com.doublenavigation.app.component;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.RuntimeModule;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaylevenApi;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.PaymentContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.HistoryContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.ShopList;
import moshimoshi.cyplay.com.doublenavigation.utils.ActionEventHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;
import ninja.cyplay.com.apilibrary.models.singleton.TweetCacheManager;

/**
 * Created by damien on 19/04/16.
 */
@Component(
        modules = {
                RuntimeModule.class
        },
        dependencies = {
                BusComponent.class,
        }
)
public interface RuntimeComponent {

    APIValue provideApiValue();

    TweetCacheManager provideTweetCacheManager();

    SellerContext provideSellerContext();

    ConfigHelper configHelper();

    CustomerContext provideCustomerContext();

    HistoryContext provideHistoryContext();

    PaymentContext providePaymentContext();

    ShopList shops();

    PaylevenApi paylevenApi();

    ActionEventHelper actionEventBuilder();

}