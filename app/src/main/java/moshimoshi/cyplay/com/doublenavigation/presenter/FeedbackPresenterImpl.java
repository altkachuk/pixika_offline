package moshimoshi.cyplay.com.doublenavigation.presenter;

import android.content.Context;

import moshimoshi.cyplay.com.doublenavigation.model.business.Feedback;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceException;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.view.FeedbackView;
import ninja.cyplay.com.apilibrary.domain.interactor.FeedbackInteractor;
import ninja.cyplay.com.apilibrary.domain.interactor.callback.ResourceRequestCallback;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFeedback;

/**
 * Created by wentongwang on 26/06/2017.
 */

public class FeedbackPresenterImpl extends BasePresenter implements FeedbackPresenter {

    private FeedbackInteractor feedbackInteractor;
    private FeedbackView feedbackView;

    public FeedbackPresenterImpl(Context context, FeedbackInteractor feedbackInteractor) {
        super(context);
        this.feedbackInteractor = feedbackInteractor;
    }

    @Override
    public void sentFeedback() {
        feedbackInteractor.addResource(feedbackView.getFeedback(), new ResourceRequestCallback() {
            @Override
            public void onSuccess(Object o) {
                feedbackView.onResourceViewResponse(new ResourceResponseEvent<PR_AFeedback>((Feedback) o,
                        null,
                        null));

            }

            @Override
            public void onError(BaseException e) {
                feedbackView.onResourceViewResponse(new ResourceResponseEvent<PR_AFeedback>(null,
                        new ResourceException(e),
                        null));
            }
        });
    }

    @Override
    public void setView(FeedbackView view) {
        this.feedbackView = view;
    }
}
