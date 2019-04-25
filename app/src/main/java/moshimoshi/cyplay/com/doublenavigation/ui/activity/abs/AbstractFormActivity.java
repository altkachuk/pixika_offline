package moshimoshi.cyplay.com.doublenavigation.ui.activity.abs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_Form;
import moshimoshi.cyplay.com.doublenavigation.model.singleton.CustomerContext;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.IntentConstants;
import moshimoshi.cyplay.com.doublenavigation.ui.activity.base.BaseActivity;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.MessageProgressDialog;
import moshimoshi.cyplay.com.doublenavigation.ui.custom.form.FormSectionCardView;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import ninja.cyplay.com.apilibrary.utils.ActivityHelper;

/**
 * Created by romainlebouc on 08/12/2016.
 */

public abstract class AbstractFormActivity<Resource> extends BaseActivity {

    @Inject
    protected CustomerContext customerContext;

    @BindView(R.id.form_view_container)
    protected LinearLayout formViewContainer;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @BindView(R.id.scroll)
    ScrollView scrollView;

    // The Form Configuration
    private PR_Form form;
    // Form is a Create or an Edit formObject form
    protected String formMode = null;

    // Customer Detail
    protected Resource formObject;
    // Yes/No Dialog
    private AlertDialog yesNoDialog;
    // Progress Dialog to display logout progress
    protected MessageProgressDialog progressDialog;

    private Map<String, FormSectionCardView> sectionsMap = new HashMap<>();
    private List<FormSectionCardView> sections = new ArrayList<>();

    // For animation purpose
    private Handler handler = new Handler();


    // -------------------------------------------------------------------------------------------
    //                                      Lifecycle
    // -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);
    }


    @Override
    protected boolean isLeftCrossClosable() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String focusTag = this.getIntent().getStringExtra(IntentConstants.EXTRA_FORM_SELECTED_TAG);
        if (focusTag != null) {
            final FormSectionCardView formSectionCardView = sectionsMap.get(focusTag);
            if (formSectionCardView != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        formSectionCardView.requestFocus();
                    }
                }, 50);
            }
        }
    }

    // -------------------------------------------------------------------------------------------
    //                                      Override(s)
    // -------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_form_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                yesNoDialog.show();
                return true;
            case R.id.action_validate_form:
                runFormValidation();
                ActivityHelper.hideSoftKeyboard(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        yesNoDialog.show();
    }


    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    // Getter for each field of the form
    public Object getObject() {
        return formObject;
    }


    protected void init() {
        initForm();
        initDialogs();
    }

    private void initForm() {
        // is Get Form Mode
        if (getIntent().hasExtra(IntentConstants.EXTRA_FORM_EDIT_MODE)) {
            formMode = getIntent().getStringExtra(IntentConstants.EXTRA_FORM_EDIT_MODE);
            // Get Form
            form = FormHelper.getFormFromKey(this, formMode, configHelper);
            createSections();
        } else {
            // WHAT TODO ?
        }
    }

    private void initDialogs() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        // We leave the form modification refused
                        yesNoDialog.dismiss();
                        finishForm(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        // do nothing we stay on the form
                        yesNoDialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AlertDialog);
        yesNoDialog = builder
                .setTitle(R.string.warning)
                .setMessage(R.string.form_confirm_quit_dialog)
                .setPositiveButton(android.R.string.ok, dialogClickListener)
                .setNegativeButton(R.string.cancel, dialogClickListener)
                .create();
        // Progress Dialog
        progressDialog = new MessageProgressDialog(this);
    }

    private void createSections() {
        if (form != null && form.getSectionsDescriptors() != null) {
            //sections = new ArrayList<>();
            for (int i = 0; i < form.getSectionsDescriptors().size(); i++) {
                // create Section
                FormSectionCardView sectionCardView = new FormSectionCardView(this);
                sectionCardView.setActivity(this);
                formViewContainer.addView(sectionCardView);
                // Set form's Section to the view
                String sectionTag = form.getSectionsDescriptors().get(i).getTag();
                sectionCardView.setSection(FormHelper.getSectionForTag(this, sectionTag, configHelper));
                //sections.add(sectionCardView);
                sectionsMap.put(sectionTag, sectionCardView);
                sections.add(sectionCardView);
            }
        }
    }

    private void runFormValidation() {
        ActivityHelper.hideSoftKeyboard(this);
        Boolean formValid = true;
        View firstNonValidView = null;
        for (FormSectionCardView formSectionCardView : sections) {
            View viewToValidate = formSectionCardView.runValidation();
            if (viewToValidate != null) {
                firstNonValidView = firstNonValidView == null ? viewToValidate : firstNonValidView;
                formValid = false;
            }
        }
        if (firstNonValidView != null) {
            this.scrollView.smoothScrollTo(0, (int) firstNonValidView.getY());
        }
        if (formValid) {
            saveObject();
        }
    }

    protected abstract void saveObject();

    public void finishForm(boolean canceled) {
        if (canceled){
            this.setResult(Activity.RESULT_CANCELED);
        }
        this.supportFinishAfterTransition();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_out_bottom);
    }
}
