package moshimoshi.cyplay.com.doublenavigation.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Created by romainlebouc on 01/02/2017.
 */

public class ReflexiveComparator implements Comparator<Object> {

    final String field;
    final int way;

    public ReflexiveComparator(String field, int way) {
        this.field = field;
        this.way = way;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        try {
            Field lhsField = lhs.getClass().getDeclaredField(field);
            Comparable lhsComparable = (Comparable) lhsField.get(lhs);

            Field rhsField = rhs.getClass().getDeclaredField(field);
            Comparable rhsComparable = (Comparable) rhsField.get(rhs);
            return way * ListUtils.compare(lhsComparable, rhsComparable);


        } catch (Exception e) {
            Log.i(ConfigHelper.class.getName(), e.getMessage(), e);
        }

        return 0;
    }

}
