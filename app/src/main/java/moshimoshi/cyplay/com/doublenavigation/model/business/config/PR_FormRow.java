package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EValuesSource;

/**
 * Created by damien on 16/03/16.
 */
@Parcel
public class PR_FormRow {

    public String tag;
    public String label;
    public String placeholder;
    public String values_source;
    public List<PR_FormRowValue> values;
    public String defaultValue;
    public String type;
    public String validator;

    public String getTag() {
        return tag;
    }

    public String getLabel() {
        return label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public List<PR_FormRowValue> getValues() {
        return values;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getType() {
        return type;
    }

    public String getValidator() {
        return validator;
    }

    public EValuesSource getValuesSource(){
        return EValuesSource.getEValuesSourceCode(values_source);
    }

}
