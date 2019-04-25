package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by romainlebouc on 26/07/16.
 */
@Parcel
public class ProductSpecification implements Serializable, Comparable {

    String id;
    String value_id;
    String spec ;
    String value ;
    String unit;
    Media medias;
    List<String> sections;

    public String getId() {
        return id;
    }

    public String getValue_id() {
        return value_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Media getMedias() {
        return medias;
    }

    public void setMedias(Media medias) {
        this.medias = medias;
    }

    public List<String> getSections() {
        return sections;
    }

    public String getValueUnit(){
        String result = "";
        if ( value !=null){
            result+=value;
        }
        if ( unit !=null){
            result+= " " + unit;
        }
        return result;
    }

    // For Adapter
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (value != null)
            builder.append(value);
        builder.append(" ");
        if (unit != null)
            builder.append(unit);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSpecification that = (ProductSpecification) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return unit != null ? unit.equals(that.unit) : that.unit == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        ProductSpecification other = (ProductSpecification) o;
        return this.getValue().compareTo(other.getValue());
    }
}
