package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductShare;
import moshimoshi.cyplay.com.doublenavigation.view.CreateResourceView;


/**
 * Created by romainlebouc on 24/08/16.
 */
public interface ProductSharePresenter extends Presenter<CreateResourceView<ProductShare>>{

    void shareProducts(ProductShare productShare);
}
