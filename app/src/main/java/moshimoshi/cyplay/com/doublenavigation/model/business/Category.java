package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACategory;

/**
 * Created by romainlebouc on 12/07/2014.
 */
@Parcel
public class Category extends PR_ACategory {

    String id;
    String name;
    String image;
    Boolean has_sub_families;
    String path;
    String parent;

    public Category() {

    }

    // Cpy __ctor
    public Category(Category other) {
        this.id = other.id;
        this.name = other.name;
        this.image = other.image;
        this.has_sub_families = other.has_sub_families;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getHas_sub_families() {
        return has_sub_families;
    }

    public String getPath() {
        return path;
    }

    public void setHas_sub_families(Boolean has_sub_families) {
        this.has_sub_families = has_sub_families;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
