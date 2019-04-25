package moshimoshi.cyplay.com.doublenavigation.utils;

/**
 * Created by romainlebouc on 22/08/16.
 */
public class ListUtils {

    public static <T extends Comparable<T>> int compare(T a, T b) {
        return
                a==null ?
                        (b==null ? 0 : Integer.MIN_VALUE) :
                        (b==null ? Integer.MAX_VALUE : a.compareTo(b));
    }

}
