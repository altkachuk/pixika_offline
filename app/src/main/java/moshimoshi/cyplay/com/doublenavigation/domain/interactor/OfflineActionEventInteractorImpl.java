package moshimoshi.cyplay.com.doublenavigation.domain.interactor;

import java.util.ArrayList;
import java.util.List;

import atproj.cyplay.com.dblibrary.db.IDatabaseHandler;
import ninja.cyplay.com.apilibrary.domain.executor.InteractorExecutor;
import ninja.cyplay.com.apilibrary.domain.executor.MainThreadExecutor;
import ninja.cyplay.com.apilibrary.domain.interactor.AbstractInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.ActionEventInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.ActionEvent;

/**
 * Created by andre on 30-Aug-18.
 */

public class OfflineActionEventInteractorImpl extends AbstractInteractor implements ActionEventInteractor {

    private IDatabaseHandler _databaseHandler;


    public OfflineActionEventInteractorImpl(InteractorExecutor interactorExecutor, MainThreadExecutor mainThreadExecutor, IDatabaseHandler databaseHandler) {
        super(interactorExecutor, mainThreadExecutor);
        _databaseHandler = databaseHandler;
    }

    @Override
    public void saveActionEvents(List<ActionEvent> pageViews, final ResourceRequestCallback<List<ActionEvent>> callback) {

        final List<ActionEvent> result = new ArrayList<>();

        getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }
}
