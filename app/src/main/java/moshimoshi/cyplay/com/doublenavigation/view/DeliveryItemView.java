package moshimoshi.cyplay.com.doublenavigation.view;


import java.util.List;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;

/**
 * Created by wentongwang on 29/03/2017.
 */

public interface DeliveryItemView extends View {
    void onUpdateSuccess();
    void onUpdateError(List<BaseException> exceptions );
}
