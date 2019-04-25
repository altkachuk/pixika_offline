package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 16/03/16.
 */
@Parcel
public class PR_Form {

    String tag;
    List<PR_FormDescriptor> sectionsDescriptors;

    public String getTag() {
        return tag;
    }

    public List<PR_FormDescriptor> getSectionsDescriptors() {
        return sectionsDescriptors;
    }

}
