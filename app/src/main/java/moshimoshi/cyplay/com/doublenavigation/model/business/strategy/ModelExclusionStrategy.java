package moshimoshi.cyplay.com.doublenavigation.model.business.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.lang.annotation.Annotation;
import java.util.Collection;

import ninja.cyplay.com.apilibrary.models.ReadOnlyModelField;

/**
 * Created by romainlebouc on 05/12/2016.
 */

public class ModelExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {


        Collection<Annotation> annotationCollections =  f.getAnnotations();
        for ( Annotation annotation : annotationCollections){
            if (annotation instanceof ReadOnlyModelField){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
