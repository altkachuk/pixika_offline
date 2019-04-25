package moshimoshi.cyplay.com.doublenavigation.model.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wentongwang on 27/03/2017.
 */

public class ModelsWithType<Model> {

    private String type;
    private List<Model> models = new ArrayList<>();
    private int currentSelectedModelPos = 0;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public int getCurrentSelectedModelPos() {
        return currentSelectedModelPos;
    }

    public void setCurrentSelectedModelPos(int currentSelectedModelPos) {
        this.currentSelectedModelPos = currentSelectedModelPos;
    }

    public Model getSelectedModel(){
        return models.get(currentSelectedModelPos);
    }

    public boolean sameType(String type) {
        return type.equals(this.type);
    }

    public void clearModels(){
        models.clear();
    }
}
