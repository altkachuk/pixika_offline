package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 16/03/16.
 */
@Parcel
public class PR_FormConfig {

    List<PR_Form> forms;
    List<PR_FormRow> rows;
    List<PR_FormSection> sections;
    AddressSuggestionConfig address_suggestion;

    public List<PR_Form> getForms() {
        return forms;
    }

    public List<PR_FormRow> getRows() {
        return rows;
    }

    public List<PR_FormSection> getSections() {
        return sections;
    }

    public AddressSuggestionConfig getAddressSuggestionConfig() {
        return address_suggestion != null ? address_suggestion : new AddressSuggestionConfig();
    }
}
