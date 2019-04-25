package moshimoshi.cyplay.com.doublenavigation.app.component;

import dagger.Component;
import moshimoshi.cyplay.com.doublenavigation.app.module.OfflineInteractorModule;
import ninja.cyplay.com.apilibrary.domain.component.ApplicationComponent;
import ninja.cyplay.com.apilibrary.domain.component.ExecutorComponent;
import ninja.cyplay.com.apilibrary.domain.interactor.ActionEventInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.BasketInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CatalogueInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ClientInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.CustomerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ProductInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SaleInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.SellerInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.WishlistInteractor;

/**
 * Created by andre on 13-Aug-18.
 */

@Component(
        modules = {
                OfflineInteractorModule.class
        },
        dependencies = {
                ApplicationComponent.class,
                DBComponent.class,
                ExecutorComponent.class
        }
)
public interface OfflineInteractorComponent {

    ClientInteractor clientInteractor();
    SellerInteractor sellerInteractor();
    BasketInteractor basketInteractor();
    CatalogueInteractor catalogueInteractor();
    ProductInteractor productInteractor();
    CustomerInteractor customerInteractor();
    ActionEventInteractor actionEventInteractor();
    WishlistInteractor wishlistInteractor();
    SaleInteractor saleInteractor();
}
