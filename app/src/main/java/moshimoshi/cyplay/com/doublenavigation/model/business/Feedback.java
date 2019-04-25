package moshimoshi.cyplay.com.doublenavigation.model.business;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EFeedbackType;
import ninja.cyplay.com.apilibrary.models.ModelResource;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AFeedback;

/**
 * Created by wentongwang on 26/06/2017.
 */
@ModelResource
public class Feedback extends PR_AFeedback {

    String seller_id;
    String shop_id;
    String text;
    String feedback_type;
    String os;
    String app_version;
    String device_id;
    Integer screen;


    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EFeedbackType getFeedback_type() {
        return EFeedbackType.getFeedBackTypeByCode(feedback_type);
    }

    public void setFeedback_type(EFeedbackType feedback_type) {
        this.feedback_type = feedback_type.getCode();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }
}
