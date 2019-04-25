package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 17/03/16.
 */
@Parcel
public class PR_FormSection {

    String tag;
    String label;
    String icon;
    List<PR_FormDescriptor> rowsDescriptors;
    PR_FormDescriptor templateRowDescriptor;

    public String getTag() {
        return tag;
    }

    public String getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public List<PR_FormDescriptor> getRowsDescriptors() {
        return rowsDescriptors;
    }

    public PR_FormDescriptor getTemplateRowDescriptor() {
        return templateRowDescriptor;
    }

}
