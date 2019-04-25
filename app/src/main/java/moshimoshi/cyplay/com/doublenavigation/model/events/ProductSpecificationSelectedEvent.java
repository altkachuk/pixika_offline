package moshimoshi.cyplay.com.doublenavigation.model.events;

import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;

/**
 * Created by damien on 16/11/16.
 */
public class ProductSpecificationSelectedEvent {

    private Integer level;

    private ProductSpecification specification;

    public ProductSpecificationSelectedEvent(ProductSpecification specification, Integer level) {
        this.specification = specification;
        this.level = level;
    }

    public ProductSpecification getSpecification() {
        return specification;
    }

    public Integer getLevel() {
        return level;
    }
}
