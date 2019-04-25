package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSuggestion;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 05/05/15.
 */
public interface ProductSuggestionPresenter extends Presenter<ResourceView<ProductSuggestion>> {

    void suggestProducts(String productName);

}
