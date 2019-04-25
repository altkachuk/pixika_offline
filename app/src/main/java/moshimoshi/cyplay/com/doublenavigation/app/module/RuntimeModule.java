package moshimoshi.cyplay.com.doublenavigation.app.module;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
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
@Module
public class RuntimeModule {

    private final APIValue apiValue;
    private final TweetCacheManager tweetCacheManager;
    private final SellerContext sellerContext;
    private final CustomerContext customerContext;
    private final HistoryContext historyContext;
    private final PaymentContext paymentContext;
    private final ShopList shopList;
    private final PaylevenApi paylevenApi;
    private final ActionEventHelper actionEventHelper;
    private final ConfigHelper configHelper;

    public RuntimeModule(Context context, EventBus bus) {
        this.apiValue = new APIValue(context);
        this.tweetCacheManager = new TweetCacheManager();
        this.sellerContext = new SellerContext(context, tweetCacheManager);
        this.customerContext = new CustomerContext(context, bus);
        this.paymentContext = new PaymentContext();
        this.historyContext = new HistoryContext(context);
        this.shopList = new ShopList();
        this.paylevenApi = new PaylevenApi();
        this.configHelper = new ConfigHelper();
        this.actionEventHelper = new ActionEventHelper(context, sellerContext, customerContext, configHelper);

    }

    @Provides
    public APIValue provideApiValue() {
        return apiValue;
    }

    @Provides
    public TweetCacheManager provideTweetCacheManager() {
        return tweetCacheManager;
    }

    @Provides
    public SellerContext provideSellerContext() {
        return sellerContext;
    }

    @Provides
    public CustomerContext provideCustomerContext() {
        return customerContext;
    }

    @Provides
    public HistoryContext provideHistoryContext() {
        return historyContext;
    }

    @Provides
    public PaymentContext providePaymentContext() {
        return paymentContext;
    }


    @Provides
    public ShopList shops() {
        return shopList;
    }

    @Provides
    public PaylevenApi paylevenApi() {
        return paylevenApi;
    }

    @Provides
    public ActionEventHelper actionEventBuilder() {
        return actionEventHelper;
    }

    @Provides
    public ConfigHelper configHelper() {
        return configHelper;
    }

}
