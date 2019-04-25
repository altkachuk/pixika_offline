package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.Product;
import moshimoshi.cyplay.com.doublenavigation.model.business.Shop;
import moshimoshi.cyplay.com.doublenavigation.model.business.Sku;
import moshimoshi.cyplay.com.doublenavigation.view.OpinionView;
import ninja.cyplay.com.apilibrary.models.business.ECustomerOpinion;

/**
 * Created by damien on 15/05/15.
 */
public interface WishlistPresenter extends Presenter<OpinionView> {

    void toggleWishlistItem(ECustomerOpinion opinion, Product product, Sku sku, Shop shop);

}
