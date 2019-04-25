package moshimoshi.cyplay.com.doublenavigation.ui.custom.form;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import moshimoshi.cyplay.com.doublenavigation.R;
import moshimoshi.cyplay.com.doublenavigation.app.PlayRetailApp;
import moshimoshi.cyplay.com.doublenavigation.utils.ConfigHelper;
import moshimoshi.cyplay.com.doublenavigation.utils.FormHelper;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormDescriptor;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormRow;
import moshimoshi.cyplay.com.doublenavigation.model.business.config.PR_FormSection;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;

/**
 * Created by damien on 11/05/16.
 */
public class FormSectionCardView extends FormSubView {

    @Inject
    ConfigHelper configHelper;

    @Nullable
    @BindView(R.id.section_title_text_view)
    TextView sectionTitle;

    @Nullable
    @BindView(R.id.section_field_container)
    LinearLayout sectionFieldContainer;

    private Activity activity;

    private PR_FormSection section;

    private List<FormSubView> formFields;

    public FormSectionCardView(Context context) {
        super(context);
        init(context);
    }

    public FormSectionCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormSectionCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void init(Context context) {
        PlayRetailApp.get(context).inject(this);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_view_form_section, this, true);
        // Bind xml
        ButterKnife.bind(this);
        // update Design
        updateDesign();
    }

    // -------------------------------------------------------------------------------------------
    //                                      Method(s)
    // -------------------------------------------------------------------------------------------

    private void updateDesign() {
        sectionTitle.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
    }

    public void setSection(PR_FormSection section) {
        this.section = section;
        fillSection();
    }

    private void fillSection() {
        if (section != null) {
            if (section.getLabel() != null && section.getLabel().length() > 0) {
                sectionTitle.setVisibility(VISIBLE);
                String label = StringUtils.getStringResourceByName(getContext(), section.getLabel());
                sectionTitle.setText(label);
            } else {
                sectionTitle.setVisibility(GONE);
            }
            createFields();
        }
    }

    private void createFields() {
        formFields = new ArrayList<>();
        if (section.getRowsDescriptors() != null && section.getRowsDescriptors().size() > 0) {
            for (int i = 0; i < section.getRowsDescriptors().size(); i++) {
                // Get Descriptor
                PR_FormDescriptor descriptor = section.getRowsDescriptors().get(i);
                FormSubView subViewLayout;
                if (descriptor != null) {
                    // Get Row 'tagged' by the descriptor
                    String tag = descriptor.getTag();
                    PR_FormRow row = configHelper.getRowForTag(this.getContext(), tag, configHelper);
                    // Add the correct view from the row's tag
                    if (row != null) {
                        subViewLayout = FormHelper.getViewFromTypeName(activity, row.getType());
                        // If view found
                        if (subViewLayout != null) {
                            subViewLayout.setDescriptor(descriptor);
                            subViewLayout.setRow(row);
                            sectionFieldContainer.addView(subViewLayout);
                            formFields.add(subViewLayout);
                        }
                    }
                }
            }
        }
        // Repetitive field
        else if (section.getTemplateRowDescriptor() != null) {
            // Get Row 'tagged' by the descriptor
            PR_FormRow row = configHelper.getRowForTag(this.getContext(), section.getTemplateRowDescriptor().getTag(), configHelper);
            // Add the correct view from the row's tag
            if (row != null && row.getType() != null) {
                FormRepetableSubView formRepetableSubView = new FormRepetableSubView(getContext());
                formRepetableSubView.setDescriptor(section.getTemplateRowDescriptor());
                formRepetableSubView.setRow(row);
                sectionFieldContainer.addView(formRepetableSubView);
                formFields.add(formRepetableSubView);
            }
        }
    }

    @Override
    public View runValidation() {
        View firstNonValidView = null;
        if (formFields != null) {
            for (int i = 0; i < formFields.size(); i++) {
                if (formFields.get(i).runValidation() != null) {
                    firstNonValidView = firstNonValidView == null ? formFields.get(i) : firstNonValidView;
                }
            }
        }
        return firstNonValidView;
    }

    @Override
    public void setValue(Object val) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
