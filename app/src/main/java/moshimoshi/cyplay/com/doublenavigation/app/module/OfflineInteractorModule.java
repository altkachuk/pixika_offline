package moshimoshi.cyplay.com.doublenavigation.app.module;

import android.content.Context;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import dagger.Module;
import dagger.Provides;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineActionEventInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineBasketInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineCatalogueInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineClientInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineCustomerInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineProductInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineSaleInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineSellerInteractorImpl;
import moshimoshi.cyplay.com.doublenavigation.domain.interactor.OfflineWishlistInteractorImpl;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
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

@Module
public class OfflineInteractorModule {

    @Provides
    ClientInteractor clientInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineClientInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    SellerInteractor sellerInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineSellerInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    BasketInteractor basketInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineBasketInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    CatalogueInteractor catalogueInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineCatalogueInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    ProductInteractor productInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler, Context context) {
        return new OfflineProductInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler, context);
    }

    @Provides
    CustomerInteractor customerInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineCustomerInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    ActionEventInteractor actionEventInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineActionEventInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    WishlistInteractor wishlistInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineWishlistInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }

    @Provides
    SaleInteractor saleInteractor(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        return new OfflineSaleInteractorImpl(interactorExecutor, mainThreadExecutor, databaseHandler);
    }
}
