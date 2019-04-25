package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.util.List;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFilterCheck;

/**
 * Created by anishosni on 10/06/2015.
 */
@Parcel
public class FilterCheck extends PR_AFilterCheck {

    List<String> values;
    String spec;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }


}
