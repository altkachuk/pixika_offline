package moshimoshi.cyplay.com.doublenavigation.view;

import java.util.List;

/**
 * Created by damien on 05/05/15.
 */
public interface CustomerCompletionView extends BaseView {

    void showLoading();

    void onCompletionSuccess(List<String> customers);

}