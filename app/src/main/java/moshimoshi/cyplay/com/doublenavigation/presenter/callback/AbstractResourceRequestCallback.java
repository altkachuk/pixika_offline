package moshimoshi.cyplay.com.doublenavigation.presenter.callback;

import android.util.Log;

import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.utils.LogUtils;

/**
 * Created by romainlebouc on 09/08/16.
 */
public abstract class AbstractResourceRequestCallback<Resource> implements ResourceRequestCallback<Resource> {


    public abstract void onSuccess(Resource resource);

    public abstract void onError(BaseException e);

    public void onSafeExecute(SafeResourceRequestCallbackExecutor safeResourceRequestCallbackExecutor) {

        if (safeResourceRequestCallbackExecutor != null) {
            try {
                safeResourceRequestCallbackExecutor.execute();
            } catch (Exception e) {
                Log.e(LogUtils.generateTag(this), "Error will executing Resource Callback", e);
            }
        }

    }


}
