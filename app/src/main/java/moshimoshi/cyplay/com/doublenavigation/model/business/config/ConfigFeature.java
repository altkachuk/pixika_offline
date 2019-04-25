package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import android.support.annotation.NonNull;

import org.parceler.Parcel;


/**
 * Created by romainlebouc on 14/08/16.
 */
@Parcel
public class ConfigFeature {

    Appearance appearanceConfig;
    DisplayConfig displayConfig;
    Parameters additional_parameters;
    Modules modules;
    Home PRAHome;
    Calendar calendar;
    HomeConfig homeConfig;
    PR_FormConfig formConfig;
    ProductConfig productConfig;
    PaymentConfig paymentConfig;
    CustomerConfig customerConfig;
    FeedbackConfig feedbackConfig;
    AuthenticationConfig authenticationConfig;
    WebViewConfig webviewConfig;

    public Appearance getAppearanceConfig() {
        return appearanceConfig;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public Modules getModules() {
        return modules;
    }

    public Home getPRAHome() {
        return PRAHome;
    }

    public Parameters getAdditional_parameters() {
        return additional_parameters;
    }

    public
    @NonNull
    HomeConfig getHomeConfig() {
        return homeConfig != null ? homeConfig : new HomeConfig();
    }

    public FeedbackConfig getFeedbackConfig() {
        return feedbackConfig;
    }

    public PR_FormConfig getFormConfig() {
        return formConfig;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public ProductConfig getProductConfig() {
        if (productConfig == null) {
            productConfig = new ProductConfig();
        }
        return productConfig;
    }

    public
    @NonNull
    AuthenticationConfig getAuthenticationConfig() {
        return authenticationConfig != null ? authenticationConfig : new AuthenticationConfig();
    }

    public CustomerConfig getCustomerConfig() {
        return customerConfig;
    }

    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public WebViewConfig getWebviewConfig() {
        return webviewConfig;
    }
}
