package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.meta.ResourceFieldSorting;

/**
 * Created by romainlebouc on 28/09/16.
 */
@Parcel
public class ProductSearchSortConfig {

    String label;
    Integer way;
    String field;

    public ProductSearchSortConfig(){}

    public ProductSearchSortConfig(ResourceFieldSorting resourceFieldSorting){
        this.label= resourceFieldSorting.getLabel();
        this.way = resourceFieldSorting.getSortingWay().getCode();
        this.field = resourceFieldSorting.getField();
    }


    public String getLabel() {
        return label;
    }

    public Integer getWay() {
        return way;
    }

    public String getField() {
        return field;
    }
}
