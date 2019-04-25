package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.sections.SkuSpecificationSection;

/**
 * Created by romainlebouc on 12/12/2016.
 */
@Parcel
public class ProductSpecificationsDisplayConfig {

    List<ProductSpecificationsSectionsDisplayConfig> sections;
    List<ProductSpecificationsSpecDisplayConfig> specs;

    public List<ProductSpecificationsSectionsDisplayConfig> getSections() {
        return sections;
    }

    public List<ProductSpecificationsSpecDisplayConfig> getSpecs() {
        return specs;
    }

    public void sortSpecificationSections(List<SkuSpecificationSection> skuSpecificationSections) {
        final List<String> sectionIds = getSectionIds();
        Collections.sort(skuSpecificationSections, new Comparator<SkuSpecificationSection>() {
            @Override
            public int compare(SkuSpecificationSection lhs, SkuSpecificationSection rhs) {
                return sectionIds.indexOf(lhs.getId()) - sectionIds.indexOf(rhs.getId());
            }
        });
    }

    private List<String> getSectionIds() {
        List<String> result = new ArrayList<>();
        if (sections != null) {
            for (ProductSpecificationsSectionsDisplayConfig section : sections) {
                result.add(section.getSection_id());
            }
        }
        return result;
    }
}
