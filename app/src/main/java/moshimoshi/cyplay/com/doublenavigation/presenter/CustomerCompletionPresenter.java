package moshimoshi.cyplay.com.doublenavigation.presenter;

import moshimoshi.cyplay.com.doublenavigation.view.CustomerCompletionView;

/**
 * Created by damien on 05/05/15.
 */
public interface CustomerCompletionPresenter extends Presenter<CustomerCompletionView> {

    void completionCustomer(String customer);

}
