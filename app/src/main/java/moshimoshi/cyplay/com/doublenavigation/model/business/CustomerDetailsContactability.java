package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import ninja.cyplay.com.apilibrary.models.ModelField;

@Parcel
public class CustomerDetailsContactability {

    @ModelField(eventLogging = true)
    Boolean mail;

    @ModelField(eventLogging = true)
    Boolean email ;

    @ModelField(eventLogging = true)
    Boolean phone ;

    @ModelField(eventLogging = true)
    Boolean newsletter;

    @ModelField(eventLogging = true)
    Boolean sms;

    @ModelField(eventLogging = true)
    Boolean partner_mail;

    @ModelField(eventLogging = true)
    Boolean partner_email;

    @ModelField(eventLogging = true)
    Boolean partner_phone;

    @ModelField(eventLogging = true)
    Boolean partner_newsletter;

    @ModelField(eventLogging = true)
    Boolean partner_sms;

    public Boolean getMail() {
        return mail;
    }

    public Boolean getEmail() {
        return email;
    }

    public Boolean getPhone() {
        return phone;
    }

    public Boolean getNewsletter() {
        return newsletter;
    }

    public Boolean getSms() {
        return sms;
    }

    public Boolean getPartner_mail() {
        return partner_mail;
    }

    public Boolean getPartner_email() {
        return partner_email;
    }

    public Boolean getPartner_phone() {
        return partner_phone;
    }

    public Boolean getPartner_newsletter() {
        return partner_newsletter;
    }

    public Boolean getPartner_sms() {
        return partner_sms;
    }
}
