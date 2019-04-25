package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.content.Context;

import org.parceler.Parcel;

import java.util.Date;

import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.ModelField;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_AProfessionalDetails;

@Parcel
public class ProfessionalDetails extends PR_AProfessionalDetails {

    @ModelField(eventLogging = true)
    String organization_name;

    @ModelField(eventLogging = true)
    String code;

    @ModelField(eventLogging = true)
    String ape_code;

    @ModelField(eventLogging = true)
    String siret_number;

    @ModelField(eventLogging = true)
    Date work_force;

    @ModelField(eventLogging = true)
    String activity;

    @ModelField(eventLogging = true)
    String legal_form;

    public ProfessionalDetails() {
        ;
    }

    public String getFormatName(Context context) {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(organization_name != null && organization_name.length() > 0 ? organization_name : "");
        return builder.toString();
    }

    public String getOrganizationName() {
        return organization_name;
    }

    public String getCode() {
        return code;
    }

    public void setOrganizationName(String organizationName) {
        this.organization_name = organizationName;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
