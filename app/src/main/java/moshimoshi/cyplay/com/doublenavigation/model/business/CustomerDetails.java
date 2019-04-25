package moshimoshi.cyplay.com.doublenavigation.model.business;

import android.content.Context;
import android.support.annotation.Nullable;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moshimoshi.cyplay.com.doublenavigation.model.business.enums.EGender;
import ninja.cyplay.com.apilibrary.models.ModelField;
import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.abstractmodels.PR_ACustomerDetails;

@Parcel
public class CustomerDetails extends PR_ACustomerDetails {

    @ModelField(eventLogging = true)
    String first_name;

    @ModelField(eventLogging = true)
    String middle_name;

    @ModelField(eventLogging = true)
    String last_name;

    @ModelField(eventLogging = true)
    Date birth_date;

    @ModelField(eventLogging = true)
    String title;

    @ModelField(eventLogging = true)
    String gender;

    @ModelField(eventLogging = true)
    String nationality;

    @ModelField(eventLogging = true)
    String hear_about_us;

    @ModelField(eventLogging = true)
    String creation_shop_id;

    @ModelField(eventLogging = true)
    String main_shop_id;

    @ModelField(eventLogging = true)
    Boolean creation_permission;

    @ModelField(eventLogging = true)
    String comment;

    @ModelField(embedded = true, eventLogging = true)
    Mail mail;

    @ModelField(embedded = true, eventLogging = true)
    CustomerDetailsEmail email;

    @ModelField(eventLogging = true)
    List<Media> medias;

    @ModelField(embedded = true, eventLogging = true)
    CustomerDetailsPhone phone;

    @ModelField(embedded = true, eventLogging = true)
    CustomerDetailsContactability contactability;

    @ModelField(embedded = true)
    CustomerMeasurements measurements;

    @ModelField(embedded = true)
    CustomerPaymentMeans payment;

    public CustomerDetails() {
        this.mail = new Mail();
        this.email = new CustomerDetailsEmail();
        this.phone = new CustomerDetailsPhone();
        this.contactability = new CustomerDetailsContactability();
        this.measurements = new CustomerMeasurements();
    }

    public void updateFromAddress(@Nullable Address address) {
        if (address != null) {
            this.first_name = address.getFirst_name();
            this.last_name = address.getLast_name();

            this.mail = address.getMail();
            this.phone.updateFromOther(address.getPhone());
            this.email.updateFromOther(address.getEmail());
        }


    }


    public String getFormatName(Context context) {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(title != null && title.length() > 0 ? StringUtils.getStringResourceByName(context, title) + " " : "");
        builder.append(last_name != null && last_name.length() > 0 ? last_name + " " : "");
        builder.append(first_name != null && first_name.length() > 0 ? first_name : "");
        return builder.toString();
    }

    public EGender getEGender() {
        EGender gender = EGender.valueFromCode(this.getGender());
        if (gender == null) {
            gender = EGender.UNKNOWN;
        }
        return gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return StringUtils.getEmptyIfNull(last_name);
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public String getTitle() {
        return StringUtils.getEmptyIfNull(title);
    }

    public String getGender() {
        return StringUtils.getEmptyIfNull(gender);
    }

    public String getNationality() {
        return StringUtils.getEmptyIfNull(nationality);
    }

    public String getHear_about_us() {
        return StringUtils.getEmptyIfNull(hear_about_us);
    }

    public CustomerDetailsEmail getEmail() {
        return email;
    }

    public Mail getMail() {
        return mail;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public void addMedia(Media media) {
        if (medias == null) {
            medias = new ArrayList<>();
        }

        medias.add(media);
    }

    public CustomerDetailsPhone getPhone() {
        return phone;
    }

    public CustomerDetailsContactability getContactability() {
        return contactability;
    }

    public CustomerMeasurements getMeasurements() {
        return measurements;
    }

    public void setCreation_shop_id(String creation_shop_id) {
        this.creation_shop_id = creation_shop_id;
    }

    public void setMain_shop_id(String main_shop_id) {
        this.main_shop_id = main_shop_id;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Boolean getCreationPermission() {
        return creation_permission;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public void setEmail(CustomerDetailsEmail email) {
        this.email = email;
    }
}
