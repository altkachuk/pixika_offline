package moshimoshi.cyplay.com.doublenavigation.utils;

import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import moshimoshi.cyplay.com.doublenavigation.model.business.Address;
import moshimoshi.cyplay.com.doublenavigation.model.business.Basket;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.BasketOffer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Payment;
import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerPaymentTransaction;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeliveryMode;
import moshimoshi.cyplay.com.doublenavigation.model.business.Fee;
import moshimoshi.cyplay.com.doublenavigation.model.business.PaymentShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReview;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductReviewAttribute;
import moshimoshi.cyplay.com.doublenavigation.model.business.PurchaseCollection;
import moshimoshi.cyplay.com.doublenavigation.model.business.WishlistItem;
import moshimoshi.cyplay.com.doublenavigation.model.business.strategy.ModelExclusionStrategy;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.factory.ExclusionStrategyFactory;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import moshimoshi.cyplay.com.doublenavigation.model.business.BarCodeInfo;
import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.DeviceConfiguration;
import moshimoshi.cyplay.com.doublenavigation.model.business.Event;
import moshimoshi.cyplay.com.doublenavigation.model.business.Offer;
import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.filters.ProductFilter;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import moshimoshi.cyplay.com.doublenavigation.model.business.Seller;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Ticket;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.Config;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.factory.ModelFactory;
import ninja.cyplay.com.apilibrary.models.APIResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ADeviceConfiguration;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProductFilter;

/**
 * Created by damien on 10/03/16.
 */

public class ClientSpec {


    public final static List<Class> MODEL_CLASSES = Arrays.asList(new Class[]{Customer.class,
            Shop.class,
            Config.class,
            Product.class,
            Ticket.class,
            Offer.class,
            Event.class,
            CatalogueLevel.class,
            Seller.class,
            BarCodeInfo.class,
            ProductShare.class,
            ProductSuggestion.class,
            Basket.class,
            Payment.class,
            CustomerPaymentTransaction.class,
            Address.class,
            ProductReview.class,
            ProductReviewAttribute.class,
            WishlistItem.class,
            BasketItem.class,
            Fee.class,
            DeliveryMode.class,
            PurchaseCollection.class,
            PaymentShare.class,
            BasketOffer.class
    });

    public static void feedFactory() {

        for (Class klass : MODEL_CLASSES) {
            Annotation annotation = Customer.class.getAnnotation(ModelResource.class);
            if (annotation == null) {
                throw new RuntimeException(klass.getName() + " should be annotated with annotation " + ModelResource.class.getName());
            }
            Set<Class<?>> allSuperTypes = ReflectionUtils.getAllSuperTypes(klass, ReflectionUtils.withAnnotation(APIResource.class));
            if (allSuperTypes == null || allSuperTypes.isEmpty()) {
                throw new RuntimeException(klass.getName() + " should inherit type having " + APIResource.class.getName() + " annotation");
            } else if (allSuperTypes.size() > 1) {
                throw new RuntimeException(klass.getName() + " should inherit only one type having " + APIResource.class.getName() + " annotation");
            } else {
                ModelFactory.getInstance().register(allSuperTypes.iterator().next(), klass);
            }

        }

        // Meta classes
        ModelFactory.getInstance().register(PR_AProductFilter.class, ProductFilter.class);

        // Special cases
        ModelFactory.getInstance().register(PR_ADeviceConfiguration.class, DeviceConfiguration.class);

        ExclusionStrategyFactory.getInstance().register(new ModelExclusionStrategy());

    }

}
