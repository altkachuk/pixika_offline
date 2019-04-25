package moshimoshi.cyplay.com.doublenavigation.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import moshimoshi.cyplay.com.doublenavigation.BuildConfig;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.Feedback;
import moshimoshi.cyplay.com.doublenavigation.model.business.FeedbackScreen;
import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EFeedbackType;
import moshimoshi.cyplay.com.doublenavigation.model.events.ResourceResponseEvent;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.SellerContext;
import moshimoshi.cyplay.com.doublenavigation.presenter.FeedbackPresenter;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.MenuBaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.adapter.ContactSelectorAdapter;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.WSErrorDialog;
import moshimoshi.cyplay.com.doublenavigation.utils.SnackBarHelper;
import moshimoshi.cyplay.com.doublenavigation.view.FeedbackView;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFeedback;
import ninja.cyplay.com.apilibrary.models.singleton.APIValue;

/**
 * Created by wentongwang on 22/06/2017.
 */

public class SupportActivity extends MenuBaseActivity implements FeedbackView {

    @Inject
    APIValue apiValue;

    @Inject
    SellerContext sellerContext;

    @Inject
    FeedbackPresenter presenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.selector)
    Spinner spinner;

    @BindView(R.id.feedback_content)
    EditText feedbackInput;

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.feedback_type)
    View feedbackTypeContainer;

    @BindView(R.id.problem)
    RadioButton bugRadioBtn;

    @BindView(R.id.suggestion)
    RadioButton suggestionRadioBtn;

    @BindView(R.id.problem_selector_layout)
    View problemSelectorLayout;

    @BindView(R.id.edit_text_for_error)
    EditText editTextForError;

    ContactSelectorAdapter adapter;
    private EFeedbackType currentType;
    private FeedbackScreen currentProblemScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initSelector();
        initFeedbackTypeLayout();
        presenter.setView(this);

        feedbackInput.requestFocus();
    }

    private List<FeedbackScreen> initSelectorList() {
        List<FeedbackScreen> result = new ArrayList<>();
        result.add(new FeedbackScreen());
        result.addAll(configHelper.getFeature().getFeedbackConfig().getScreens());

        return result;
    }

    private void initSelector() {

        adapter = new ContactSelectorAdapter(this, initSelectorList());

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentProblemScreen = position > 0 ? adapter.getItem(position) : null;
                if (position > 0) {
                    editTextForError.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }

    private void initFeedbackTypeLayout() {
        selectedBug();
    }

    @OnClick({R.id.problem_check_layout, R.id.problem})
    protected void selectedBug() {
        bugRadioBtn.setChecked(true);
        suggestionRadioBtn.setChecked(false);

        currentType = EFeedbackType.BUG;
        problemSelectorLayout.setVisibility(View.VISIBLE);

        if (spinner.getSelectedItemPosition() == 0) {
            currentProblemScreen = null;
        } else {
            currentProblemScreen = adapter.getItem(spinner.getSelectedItemPosition());
        }
    }

    @OnClick({R.id.suggestion_check_layout, R.id.suggestion})
    protected void selectedSuggestion() {
        bugRadioBtn.setChecked(false);
        suggestionRadioBtn.setChecked(true);

        currentType = EFeedbackType.SUGGESTION;
        problemSelectorLayout.setVisibility(View.GONE);
        currentProblemScreen = null;
    }


    @Override
    protected String getNavDrawerCurrentItem() {
        return EMenuAction.NAVDRAWER_ITEM_CONTACT.getCode();
    }

    @Override
    public void onError(ExceptionType exceptionType, String status, String message) {

    }

    @Override
    public Feedback getFeedback() {
        Feedback feedback = new Feedback();
        feedback.setSeller_id(sellerContext.getSeller().getId());
        feedback.setShop_id(configHelper.getCurrentShop().getId());
        feedback.setText(feedbackInput.getText().toString());
        feedback.setApp_version(BuildConfig.VERSION_NAME);
        feedback.setOs(getAndroidVersion());
        feedback.setFeedback_type(currentType);
        feedback.setDevice_id(apiValue.getDeviceId());
        feedback.setScreen(currentProblemScreen == null ? null : currentProblemScreen.getId());
        return feedback;
    }

    private String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    @Override
    public void onResourceViewResponse(ResourceResponseEvent<PR_AFeedback> resourceResponseEvent) {
        progressBar.setVisibility(View.GONE);
        if (resourceResponseEvent.getResourceException() == null) {
            SnackBarHelper.buildSnackbar(coordinatorLayout,
                    getString(R.string.send_feedback_success),
                    Snackbar.LENGTH_INDEFINITE,
                    this.getString(android.R.string.ok)).show();

            finish();

        } else {
            showErrorDialog(getString(R.string.send_feedback_error) + "\n" +
                    resourceResponseEvent.getResourceException().getMessage());
        }
    }

    @OnClick(R.id.send_button)
    public void sendFeedBack() {

        if (EFeedbackType.BUG.equals(currentType) && currentProblemScreen == null) {
            editTextForError.setError(getString(R.string.form_required_error));
            return;
        }


        if (feedbackInput.getText().toString().isEmpty()) {
            feedbackInput.setError(getString(R.string.form_required_error));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        presenter.sentFeedback();
    }

    private void showErrorDialog(String str) {
        WSErrorDialog.showWSErrorDialog(this,
                str,
                new WSErrorDialog.DialogButtonClickListener() {
                    @Override
                    public void retry() {
                        sendFeedBack();
                    }

                    @Override
                    public void cancel() {
                        finish();
                    }
                });
    }
}
