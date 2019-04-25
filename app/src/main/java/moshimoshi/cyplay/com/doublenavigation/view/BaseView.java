package moshimoshi.cyplay.com.doublenavigation.view;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;

/**
 * Created by romainlebouc on 26/04/16.
 */
public interface BaseView extends View {

    void onError(ExceptionType exceptionType, String status, String message);

}
