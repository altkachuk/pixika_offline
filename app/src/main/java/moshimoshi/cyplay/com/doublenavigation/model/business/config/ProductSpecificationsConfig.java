package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romainlebouc on 12/12/2016.
 */
@Parcel
public class ProductSpecificationsConfig {

    ProductSpecificationsDisplayConfig display;
    List<ProductDefaultSpecificationConfig> default_selection;

    public ProductSpecificationsDisplayConfig getDisplay() {
        return display == null ? new ProductSpecificationsDisplayConfig() : display;
    }

    public List<ProductDefaultSpecificationConfig> getDefault_selection() {
        return default_selection == null ? new ArrayList<ProductDefaultSpecificationConfig>() : default_selection;
    }

    public String getSectionTitle(String sectionId) {
        String result = sectionId;
        if (sectionId != null && display != null) {
            List<ProductSpecificationsSectionsDisplayConfig> sections = display.getSections();
            if (sections != null) {
                for (ProductSpecificationsSectionsDisplayConfig section : sections) {
                    if (sectionId.equals(section.getSection_id())) {
                        result = section.getTitle();
                        break;
                    }
                }
            }
        }
        return result;
    }

    public String getSpecIconURL(String specId) {
        String result = null;
        if (specId != null && display != null) {
            List<ProductSpecificationsSpecDisplayConfig> specs = display.getSpecs();
            if (specs != null) {
                for (ProductSpecificationsSpecDisplayConfig spec : specs) {
                    if (spec.getSpec_id().equals(specId)) {
                        result = spec.getIcon();
                        break;
                    }
                }
            }
        }
        return result;
    }


}
