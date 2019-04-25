package moshimoshi.cyplay.com.doublenavigation.presenter.callback;

import android.util.Log;

import java.util.List;

import ninja.cyplay.com.apilibrary.domain.interactor.callback.PaginatedResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by romainlebouc on 09/08/16.
 */
public abstract class AbstractPaginatedResourceRequestCallbackImpl<Resource> implements PaginatedResourceRequestCallback<Resource> {

    public abstract void onSuccess(List<Resource> resource, Integer count, String previous, String next);

    public abstract void onError(BaseException e);

    public void onSafeExecute(SafeResourceRequestCallbackExecutor safeResourceRequestCallbackExecutor) {

        if (safeResourceRequestCallbackExecutor != null) {
            try {
                safeResourceRequestCallbackExecutor.execute();
            } catch (Exception e) {
                Log.e(LogUtils.generateTag(this), "Error will executing Resource Callback");
            }
        }

    }
}
