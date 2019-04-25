package moshimoshi.cyplay.com.doublenavigation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ninja.cyplay.com.apilibrary.models.meta.Pagination;

/**
 * Created by romainlebouc on 17/08/16.
 */
public class URLUtils {

    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    public static Pagination getOffSetAndLimitFromQuery(URL url) throws UnsupportedEncodingException {
        Map<String, List<String>> params = URLUtils.splitQuery(url);
        int offset = params.get("offset")!=null && !params.get("offset").isEmpty() ? Integer.valueOf(params.get("offset").get(0)):-1;
        int limit = params.get("limit")!=null && !params.get("limit").isEmpty() ? Integer.valueOf(params.get("limit").get(0)):-1;
        return new Pagination(offset, limit);
    }

}
