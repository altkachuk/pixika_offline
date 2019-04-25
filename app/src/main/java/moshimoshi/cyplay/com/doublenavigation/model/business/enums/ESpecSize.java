package moshimoshi.cyplay.com.doublenavigation.model.business.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moshimoshi.cyplay.com.doublenavigation.model.business.CustomerMeasurementsShoeSize;
import moshimoshi.cyplay.com.doublenavigation.model.business.Customer;
import moshimoshi.cyplay.com.doublenavigation.model.business.ProductSpecification;

/**
 * Created by romainlebouc on 11/05/2017.
 */

public enum ESpecSize {
    SHOE_SIZE("shoe_size") {
        @Override
        public boolean matchCustomer(Customer customer, ProductSpecification productSpecification) {
            boolean result = false;
            if (customer != null
                    && customer.getDetails() != null
                    && customer.getDetails().getMeasurements() != null
                    && customer.getDetails().getMeasurements().getShoe_size() != null) {
                CustomerMeasurementsShoeSize shoeSize = customer.getDetails().getMeasurements().getShoe_size();
                result = shoeSize.getValue() != null && format(shoeSize.getValue()).equals(productSpecification.getValue());
            }
            return result;
        }
    };

    final String code;
    public final static List<String> SPEC_SIZE_CODE = new ArrayList<>();
    public final static Map<String, ESpecSize> SPEC_SIZE_MAP = new HashMap<>();

    public abstract boolean matchCustomer(Customer customer, ProductSpecification productSpecification);

    static {
        for (ESpecSize eSpecSize : ESpecSize.values()) {
            SPEC_SIZE_CODE.add(eSpecSize.getCode());
        }
        for (ESpecSize eSpecSize : ESpecSize.values()) {
            SPEC_SIZE_MAP.put(eSpecSize.getCode(), eSpecSize);
        }

    }

    ESpecSize(String code) {
        this.code = code;
    }

    public static ESpecSize getSpecSizeFromCode(String code) {
        return SPEC_SIZE_MAP.get(code);
    }

    public String getCode() {
        ESpecSize.values();
        return code;
    }

    public static String format(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

}
