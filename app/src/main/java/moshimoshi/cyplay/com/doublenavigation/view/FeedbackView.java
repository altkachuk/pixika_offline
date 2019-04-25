package moshimoshi.cyplay.com.doublenavigation.view;

import moshimoshi.cyplay.com.doublenavigation.model.business.Feedback;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFeedback;

/**
 * Created by wentongwang on 26/06/2017.
 */

public interface FeedbackView extends ResourceView<PR_AFeedback> {
    Feedback getFeedback();
}
