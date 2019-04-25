package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.model.business.CatalogueLevel;
import moshimoshi.cyplay.com.doublenavigation.view.ResourceView;

/**
 * Created by damien on 04/05/15.
 */
public interface CataloguePresenter extends Presenter<ResourceView<CatalogueLevel>> {

    void getCatalogFromCategory(String catalogLevel, String label);

}